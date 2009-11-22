/*
 * Copyright 2009 Michael Chermside
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.mcherm.zithiacharsheet.server;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Text;
import com.mcherm.zithiacharsheet.client.util.JSONSerializerBase;


/**
 * This is a servlet which exists to dump the entire contents of the database to a file
 * from which it can be restored for backup and/or archiving purposes.
 */
public class DumpDBServlet extends HttpServlet {

    private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();


    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException
    {
        res.setContentType("text/plain");
        res.setHeader("Content-disposition",
                      "attachment; filename=archive.txt" );
        JSONArchiveBuilder jsonArchiveBuilder = new JSONArchiveBuilder(res.getWriter());
        jsonArchiveBuilder.emitEntireDB();
    }


    private class JSONArchiveBuilder extends JSONSerializerBase {
        private final PrintWriter printWriter;

        public JSONArchiveBuilder(final PrintWriter out) {
            super(new Writer() {
                public void write(String s) {
                    out.write(s);
                }
            }, true);
            printWriter = out;
        }

        private void flush() {
            printWriter.flush();
        }

        public void emitEntireDB() {
            emitStartDict();
            emitDictItem("version", "1.0");
            emitStartDictItem(SaveCharsheetServiceImpl.CHARSHEET_ENTITY_KIND);
            emitTableAsItem(SaveCharsheetServiceImpl.CHARSHEET_ENTITY_KIND);
            emitEndDict();
        }

        private void emitTableAsItem(String tableName) {
            emitStartList();
            Query query = new Query(tableName);
            PreparedQuery preparedQuery = datastore.prepare(query);
            for (Entity entity : preparedQuery.asIterable()) {
                emitStartListItem();
                emitEntityAsItem(entity);
                flush();
            }
            emitEndList();
        }

        private void emitEntityAsItem(Entity entity) {
            emitStartDict();

            String id = KeyFactory.keyToString(entity.getKey());
            emitDictItem("id", id);

            Map<String, Object> properties = entity.getProperties();
            List<String> fieldNames = new ArrayList<String>();
            fieldNames.addAll(properties.keySet());
            Collections.sort(fieldNames);
            for (String fieldName : fieldNames) {
                Object fieldValue = properties.get(fieldName);
                if (fieldValue instanceof String) {
                    emitDictItem(fieldName, (String) fieldValue);
                } else if (fieldValue instanceof Text) {
                    // Treat all Text objects as containing JSON data:
                    String jsonContent = ((Text) fieldValue).getValue();
                    emitStartDictItem(fieldName);
                    emitRawJSON(jsonContent);
                } else {
                    throw new RuntimeException("Unsupported type.");
                }
            }

            emitEndDict();
        }
    }

}
