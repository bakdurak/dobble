package com.example.dobble.release.repositories.local.entities.converters;

import androidx.room.TypeConverter;

import com.example.dobble.release.repositories.local.entities.CommentEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SubCommentsConverter {

    @TypeConverter
    public String fromSubComments(List<CommentEntity.SubComment> subComments) {
        if (subComments == null) {
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<CommentEntity.SubComment>>() {}.getType();
        return gson.toJson(subComments, type);
    }

    @TypeConverter
    public List<CommentEntity.SubComment> toSubComments(String subCommentsStr) {
        if (subCommentsStr == null) {
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<CommentEntity.SubComment>>() {}.getType();
        return gson.fromJson(subCommentsStr, type);
    }
}
