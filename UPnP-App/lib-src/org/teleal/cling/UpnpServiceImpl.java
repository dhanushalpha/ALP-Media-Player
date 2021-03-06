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

package org.teleal.cling;

import org.teleal.cling.controlpoint.ControlPoint;
import org.teleal.cling.controlpoint.ControlPointImpl;
import org.teleal.cling.protocol.ProtocolFactory;
import org.teleal.cling.protocol.ProtocolFactoryImpl;
import org.teleal.cling.registry.Registry;
import org.teleal.cling.registry.RegistryImpl;
import org.teleal.cling.registry.RegistryListener;
import org.teleal.cling.transport.Router;
import org.teleal.cling.transport.RouterImpl;

import java.util.logging.Logger;

/**
 * @author Christian Bauer
 */
public class UpnpServiceImpl implements UpnpService {

    private static Logger log = Logger.getLogger(UpnpServiceImpl.class.getName());

    protected final UpnpServiceConfiguration configuration;
    protected final ControlPoint controlPoint;
    protected final ProtocolFactory protocolFactory;
    protected final Registry registry;
    protected final Router router;

    public UpnpServiceImpl() {
        this(new DefaultUpnpServiceConfiguration());
    }

    public UpnpServiceImpl(RegistryListener... registryListeners) {
        this(new DefaultUpnpServiceConfiguration(), registryListeners);
    }

    public UpnpServiceImpl(UpnpServiceConfiguration configuration, RegistryListener... registryListeners) {
        this.configuration = configuration;

        log.info(">>> Starting UPnP service...");

        log.info("Using configuration: " + configuration.getClass().getName());

        // Instantiation order is important: Router needs to start its network services after registry is ready

        this.protocolFactory = new ProtocolFactoryImpl(this);

        this.registry = new RegistryImpl(configuration, protocolFactory);
        for (RegistryListener registryListener : registryListeners) {
            this.registry.addListener(registryListener);
        }

        this.router = new RouterImpl(configuration, protocolFactory);

        this.controlPoint = new ControlPointImpl(configuration, protocolFactory, registry);

        log.info("<<< UPnP service started successfully");
    }

    public UpnpServiceConfiguration getConfiguration() {
        return configuration;
    }

    public ControlPoint getControlPoint() {
        return controlPoint;
    }

    public ProtocolFactory getProtocolFactory() {
        return protocolFactory;
    }

    public Registry getRegistry() {
        return registry;
    }

    public Router getRouter() {
        return router;
    }

    synchronized public void shutdown() {
        // Well, since java.util.logging has its own shutdown hook, this
        // might actually make it into the log or not...
        log.info(">>> Shutting down UPnP service...");

        getRegistry().shutdown();
        getRouter().shutdown();

        log.info("<<< UPnP service shutdown completed");
    }

}
