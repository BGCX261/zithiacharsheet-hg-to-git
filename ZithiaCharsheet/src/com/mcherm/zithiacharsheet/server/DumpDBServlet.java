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
                    emitDictItem(fieldName, ((Text) fieldValue).getValue());
                } else {
                    throw new RuntimeException("Unsupported type.");
                }
            }

            emitEndDict();
        }
    }

}
