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

package org.eclipse.jetty.websocket.common.endpoints.listeners;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.eclipse.jetty.websocket.common.EventQueue;
import org.eclipse.jetty.websocket.core.CloseStatus;
import org.eclipse.jetty.websocket.util.TextUtil;

public class ListenerBasicSocket implements WebSocketListener
{
    public EventQueue events = new EventQueue();

    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len)
    {
        events.add("onWebSocketBinary([%d], %d, %d)", payload.length, offset, len);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason)
    {
        events.add("onWebSocketClose(%s, %s)", CloseStatus.codeString(statusCode), TextUtil.quote(reason));
    }

    @Override
    public void onWebSocketConnect(Session session)
    {
        events.add("onWebSocketConnect(%s)", session);
    }

    @Override
    public void onWebSocketError(Throwable cause)
    {
        events.add("onWebSocketError((%s) %s)", cause.getClass().getSimpleName(), TextUtil.quote(cause.getMessage()));
    }

    @Override
    public void onWebSocketText(String message)
    {
        events.add("onWebSocketText(%s)", TextUtil.quote(message));
    }
}
