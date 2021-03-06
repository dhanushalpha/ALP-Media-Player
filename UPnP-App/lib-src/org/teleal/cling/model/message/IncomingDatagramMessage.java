/*
 * Copyright (C) 2010 Teleal GmbH, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.teleal.cling.model.message;

import java.net.InetAddress;


public class IncomingDatagramMessage<O extends UpnpOperation> extends UpnpMessage<O> {

    private InetAddress sourceAddress;
    private int sourcePort;

    // We need this:
    //
    // - when an M-SEARCH is received send a LOCATION header back with a
    //   reachable (by the remote control point) local address
    //
    // - when a NOTIFY discover message (can be a search response) is received we
    //   need to memorize on which local address it was received, so that the we can
    //   later give the remote device a reachable (from its point of view) local
    //   GENA callback address
    //
    private InetAddress localAddress;

    public IncomingDatagramMessage(O operation, InetAddress sourceAddress, int sourcePort, InetAddress localAddress) {
        super(operation);
        this.sourceAddress = sourceAddress;
        this.sourcePort = sourcePort;
        this.localAddress = localAddress;
    }

    protected IncomingDatagramMessage(IncomingDatagramMessage<O> source) {
        super(source);
        this.sourceAddress = source.getSourceAddress();
        this.sourcePort = source.getSourcePort();
        this.localAddress = source.getLocalAddress();
    }

    public InetAddress getSourceAddress() {
        return sourceAddress;
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public InetAddress getLocalAddress() {
        return localAddress;
    }

}
