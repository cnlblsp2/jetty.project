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

package org.eclipse.jetty.websocket.core.server;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Function;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.pathmap.PathSpecSet;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.websocket.core.FrameHandler;

public class WebSocketUpgradeHandler extends HandlerWrapper
{
    static final Logger LOG = Log.getLogger(WebSocketUpgradeHandler.class);
    final Handshaker handshaker = Handshaker.newInstance();
    final PathSpecSet paths = new PathSpecSet();
    final WebSocketNegotiator negotiator;

    public WebSocketUpgradeHandler(
        Function<Negotiation, FrameHandler> negotiate,
        String... pathSpecs)
    {
        this(WebSocketNegotiator.from(negotiate), pathSpecs);
    }

    public WebSocketUpgradeHandler(WebSocketNegotiator negotiator, String... pathSpecs)
    {
        this.negotiator = Objects.requireNonNull(negotiator);
        addPathSpec(pathSpecs);
    }

    public WebSocketNegotiator getWebSocketNegotiator()
    {
        return negotiator;
    }

    public void addPathSpec(String... pathSpecs)
    {
        if (pathSpecs != null)
        {
            for (String spec : pathSpecs)
            {
                this.paths.add(spec);
            }
        }
    }

    @Override
    public boolean handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        if (!paths.isEmpty() && !paths.test(target))
            return super.handle(target, baseRequest, request, response);

        if (handshaker.upgradeRequest(negotiator, request, response, null))
            return true;

        return baseRequest.getHttpChannel().isCommitted() || super.handle(target, baseRequest, request, response);
    }
}
