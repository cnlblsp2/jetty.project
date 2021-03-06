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

package org.eclipse.jetty.http2.client.http;

import org.eclipse.jetty.http2.HTTP2Channel;
import org.eclipse.jetty.http2.HTTP2StreamEndPoint;
import org.eclipse.jetty.http2.IStream;
import org.eclipse.jetty.http2.frames.DataFrame;
import org.eclipse.jetty.io.Connection;
import org.eclipse.jetty.util.Callback;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

public class ClientHTTP2StreamEndPoint extends HTTP2StreamEndPoint implements HTTP2Channel.Client
{
    private static final Logger LOG = Log.getLogger(ClientHTTP2StreamEndPoint.class);

    public ClientHTTP2StreamEndPoint(IStream stream)
    {
        super(stream);
    }

    @Override
    public void onData(DataFrame frame, Callback callback)
    {
        offerData(frame, callback);
    }

    @Override
    public boolean onTimeout(Throwable failure)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("idle timeout on {}: {}", this, failure);
        Connection connection = getConnection();
        if (connection != null)
            return connection.onIdleExpired();
        return true;
    }

    @Override
    public void onFailure(Throwable failure, Callback callback)
    {
        callback.failed(failure);
    }
}
