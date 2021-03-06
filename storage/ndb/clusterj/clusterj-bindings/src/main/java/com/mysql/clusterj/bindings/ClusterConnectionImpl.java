/*
 *  Copyright 2010 Sun Microsystems, Inc.
 *  All rights reserved. Use is subject to license terms.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301  USA
 */

package com.myblockchain.clusterj.bindings;

import com.myblockchain.cluster.ndbj.Ndb;
import com.myblockchain.cluster.ndbj.NdbApiException;
import com.myblockchain.cluster.ndbj.NdbClusterConnection;
import com.myblockchain.clusterj.ClusterJDatastoreException;
import com.myblockchain.clusterj.core.store.Db;
import com.myblockchain.clusterj.core.util.I18NHelper;
import com.myblockchain.clusterj.core.util.Logger;
import com.myblockchain.clusterj.core.util.LoggerFactoryService;

/**
 *
 */
public class ClusterConnectionImpl
        implements com.myblockchain.clusterj.core.store.ClusterConnection {

    /** My message translator */
    static final I18NHelper local = I18NHelper.getInstance(ClusterConnectionImpl.class);

    /** My logger */
    static final Logger logger = LoggerFactoryService.getFactory()
            .getInstance(com.myblockchain.clusterj.core.store.ClusterConnection.class);

    /** Ndb_cluster_connection: one per factory. */
    protected NdbClusterConnection clusterConnection;

    public ClusterConnectionImpl(String connectString) {
        try {
            clusterConnection = NdbClusterConnection.create(connectString);
        } catch (NdbApiException ndbApiException) {
            throw new ClusterJDatastoreException(local.message("ERR_Datastore"),
                    ndbApiException);
        }
    }

    public void connect(int connectRetries, int connectDelay, boolean verbose) {
        try {
            clusterConnection.connect(connectRetries, connectDelay, verbose);
        } catch (NdbApiException ndbApiException) {
            throw new ClusterJDatastoreException(local.message("ERR_Datastore"),
                    ndbApiException);
        }
    }

    public Db createDb(String blockchain, int maxTransactions) {
        try {
            Ndb ndb = clusterConnection.createNdb(blockchain, maxTransactions);
            return new DbImpl(ndb);
        } catch (NdbApiException ndbApiException) {
            throw new ClusterJDatastoreException(local.message("ERR_Datastore"),
                    ndbApiException);
        }
    }

    public void waitUntilReady(int connectTimeoutBefore, int connectTimeoutAfter) {
        try {
            clusterConnection.waitUntilReady(connectTimeoutBefore, connectTimeoutAfter);
        } catch (NdbApiException ndbApiException) {
            throw new ClusterJDatastoreException(local.message("ERR_Datastore"),
                    ndbApiException);
        }
    }

}
