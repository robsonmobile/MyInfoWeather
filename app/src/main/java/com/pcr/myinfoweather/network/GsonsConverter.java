package com.pcr.myinfoweather.network;

import com.google.gson.Gson;
import com.squareup.tape.FileObjectQueue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

/**
 * Created by Paula on 22/03/2015.
 */
public class GsonsConverter<T> implements FileObjectQueue.Converter<T> {

    private final Gson gson;
    private final Class<T> type;

    public GsonsConverter(Gson gson, Class<T> type) {
        this.gson = gson;
        this.type = type;
    }

    @Override public T from(byte[] bytes) {
        Reader reader = new InputStreamReader(new ByteArrayInputStream(bytes));
        return gson.fromJson(reader, type);
    }

    @Override public void toStream(T object, OutputStream bytes) throws IOException {
        Writer writer = new OutputStreamWriter(bytes);
        gson.toJson(object, writer);
        writer.close();
    }
}
