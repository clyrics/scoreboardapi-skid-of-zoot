package net.centilehcf.core.util.json;

import com.google.gson.JsonObject;

public interface JsonSerializer<T> {

	JsonObject serialize(T t);

}
