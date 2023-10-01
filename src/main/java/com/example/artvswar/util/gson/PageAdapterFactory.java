package com.example.artvswar.util.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

@Component
public class PageAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        if (typeToken.getRawType() == Page.class) {
            // Important: This is not safe; if a Page implementation other than PageImpl
            // is used this could lead to ClassCastExceptions
            @SuppressWarnings("unchecked")
            TypeAdapter<T> r = (TypeAdapter<T>) gson.getAdapter(TypeToken.get(PageImpl.class));

            return r;
        }
        return null;
    }
}
