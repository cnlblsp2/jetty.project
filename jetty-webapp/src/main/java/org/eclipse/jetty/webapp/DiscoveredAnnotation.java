//
// ========================================================================
// Copyright (c) 1995-2020 Mort Bay Consulting Pty Ltd and others.
//
// This program and the accompanying materials are made available under
// the terms of the Eclipse Public License 2.0 which is available at
// https://www.eclipse.org/legal/epl-2.0
//
// This Source Code may also be made available under the following
// Secondary Licenses when the conditions for such availability set
// forth in the Eclipse Public License, v. 2.0 are satisfied:
// the Apache License v2.0 which is available at
// https://www.apache.org/licenses/LICENSE-2.0
//
// SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
// ========================================================================
//

package org.eclipse.jetty.webapp;

import org.eclipse.jetty.util.Loader;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.resource.Resource;

/**
 * DiscoveredAnnotation
 *
 * Represents an annotation that has been discovered
 * by scanning source code of WEB-INF/classes and WEB-INF/lib jars.
 */
public abstract class DiscoveredAnnotation
{
    private static final Logger LOG = Log.getLogger(DiscoveredAnnotation.class);

    protected WebAppContext _context;
    protected String _className;
    protected Class<?> _clazz;
    protected Resource _resource; //resource it was discovered on, can be null (eg from WEB-INF/classes)

    public abstract void apply();

    public DiscoveredAnnotation(WebAppContext context, String className)
    {
        this(context, className, null);
    }

    public DiscoveredAnnotation(WebAppContext context, String className, Resource resource)
    {
        _context = context;
        _className = className;
        _resource = resource;
    }
    
    public String getClassName()
    {
        return _className;
    }

    public Resource getResource()
    {
        return _resource;
    }

    public Class<?> getTargetClass()
    {
        if (_clazz != null)
            return _clazz;

        loadClass();

        return _clazz;
    }

    private void loadClass()
    {
        if (_clazz != null)
            return;

        if (_className == null)
            return;

        try
        {
            _clazz = Loader.loadClass(_className);
        }
        catch (Exception e)
        {
            LOG.warn(e);
        }
    }
    
    @Override
    public String toString()
    {
        return getClass().getName() + "[" + getClassName() + "," + getResource() + "]";
    }
}
