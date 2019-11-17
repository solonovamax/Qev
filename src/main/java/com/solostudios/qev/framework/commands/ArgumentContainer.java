/*
 *
 * Copyright 2016 2019 solonovamax <solonovamax@12oclockpoint.com>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.solostudios.qev.framework.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class ArgumentContainer {
	
	private Map<String, Object> map;
	
	public ArgumentContainer() {
		map = new HashMap<>();
	}
	
	public ArgumentContainer(ArgumentContainer container) {
		map = new HashMap<>(container.map);
	}
	
	public boolean has(String key) {
		return this.map.containsKey(key) && this.map.get(key) != null;
	}
	
	@SuppressWarnings("WeakerAccess")
	public boolean contains(String key) {
		return this.map.containsKey(key);
	}
	
	public ArgumentContainer put(String key, int value) {
		this.insert(key, value);
		return this;
	}
	
	private ArgumentContainer insert(String key, Object value) {
		if (key == null) {
			throw new NullPointerException("Null key.");
		}
		this.map.put(key, value);
		return this;
	}
	
	public ArgumentContainer put(String key, long value) {
		this.insert(key, value);
		return this;
	}
	
	public ArgumentContainer put(String key, double value) {
		this.insert(key, value);
		return this;
	}
	
	public ArgumentContainer put(String key, float value) {
		this.insert(key, (double) value);
		return this;
	}
	
	public ArgumentContainer put(String key, boolean value) {
		this.insert(key, value);
		return this;
	}
	
	public ArgumentContainer put(String key, String value) {
		this.insert(key, value);
		return this;
	}
	
	public ArgumentContainer put(String key, Member value) {
		this.insert(key, value);
		return this;
	}
	
	public ArgumentContainer put(String key, User value) {
		this.insert(key, value);
		return this;
	}
	
	public ArgumentContainer put(String key, Role value) {
		this.insert(key, value);
		return this;
	}
	
	public ArgumentContainer put(String key, Class value) {
		this.insert(key, value);
		return this;
	}
	
	public ArgumentContainer put(String key, Object value) {
		this.insert(key, value);
		return this;
	}
	
	@SuppressWarnings("WeakerAccess")
	public boolean isNull(String key) {
		return get(key) == null;
	}
	
	public Object get(String key) {
		if (key == null) {
			throw new JSONException("Null key.");
		}
		return this.opt(key);
	}
	
	private Object opt(String key) {
		return key == null ? null : this.map.get(key);
	}
	
	public ArgumentContainer setNull(String key) {
		this.insert(key, null);
		return this;
	}
	
	public Integer getInt(String key) {
		Object object = this.get(key);
		if (object instanceof Integer) {
			return (Integer) object;
		}
		throw new NullPointerException("Key \"" + key + "\" is not an integer.");
	}
	
	public Long getLong(String key) {
		Object object = this.get(key);
		if (object instanceof Long) {
			return (Long) object;
		}
		throw new NullPointerException("Key \"" + key + "\" is not a long.");
	}
	
	public Double getDouble(String key) {
		Object object = this.get(key);
		if (object instanceof Double) {
			return (Double) object;
		}
		throw new NullPointerException("Key \"" + key + "\" is not a double.");
	}
	
	public Float getFloat(String key) {
		Object object = this.get(key);
		if (object instanceof Float) {
			return (Float) object;
		}
		if (object instanceof Double) {
			return ((Double) object).floatValue();
		}
		throw new NullPointerException("Key \"" + key + "\" is not an float.");
	}
	
	public Boolean getBoolean(String key) {
		Object object = this.get(key);
		if (object instanceof Boolean) {
			return (Boolean) object;
		}
		throw new NullPointerException("Key \"" + key + "\" is not a boolean.");
	}
	
	public String getString(String key) {
		Object object = this.get(key);
		if (object instanceof String) {
			return (String) object;
		}
		throw new NullPointerException("Key \"" + key + "\" is not a string.");
	}
	
	public Member getMember(String key) {
		Object object = this.get(key);
		if (object instanceof Member) {
			return (Member) object;
		}
		throw new NullPointerException("Key \"" + key + "\" is not a member.");
	}
	
	public User getUser(String key) {
		Object object = this.get(key);
		if (object instanceof User) {
			return (User) object;
		} else {
			if (object instanceof Member) {
				return ((Member) object).getUser();
			}
		}
		throw new NullPointerException("Key \"" + key + "\" is not a user.");
	}
	
	public Role getRole(String key) {
		Object object = this.get(key);
		if (object instanceof Role) {
			return (Role) object;
		}
		throw new NullPointerException("Key \"" + key + "\" is not a role.");
	}
	
	public Class getClass(String key) {
		Object object = this.get(key);
		if (object instanceof Class) {
			return (Class) object;
		}
		throw new NullPointerException("Key \"" + key + "\" is not a class.");
	}
	
	public String toString() {
		try {
			return this.toString(0);
		} catch (Exception e) {
			return null;
		}
	}
	
	public String toString(int indentFactor) throws JSONException {
		StringWriter w = new StringWriter();
		synchronized (w.getBuffer()) {
			return this.write(w, indentFactor, 0).toString();
		}
	}
	
	private Writer write(Writer writer, int indentFactor, int indent) throws JSONException {
		try {
			boolean   commanate = false;
			final int length    = this.length();
			writer.write('{');
			
			if (length == 1) {
				final Map.Entry<String, ?> entry = this.entrySet().iterator().next();
				final String               key   = entry.getKey();
				writer.write(quote(key));
				writer.write(':');
				if (indentFactor > 0) {
					writer.write(' ');
				}
				try {
					writeValue(writer, entry.getValue(), indentFactor, indent);
				} catch (Exception e) {
					throw new JSONException("Unable to write JSONObject value for key: " + key, e);
				}
			} else {
				if (length != 0) {
					final int newindent = indent + indentFactor;
					for (final Map.Entry<String, ?> entry : this.entrySet()) {
						if (commanate) {
							writer.write(',');
						}
						if (indentFactor > 0) {
							writer.write('\n');
						}
						indent(writer, newindent);
						final String key = entry.getKey();
						writer.write(quote(key));
						writer.write(':');
						if (indentFactor > 0) {
							writer.write(' ');
						}
						try {
							writeValue(writer, entry.getValue(), indentFactor, newindent);
						} catch (Exception e) {
							throw new JSONException("Unable to write JSONObject value for key: " + key, e);
						}
						commanate = true;
					}
					if (indentFactor > 0) {
						writer.write('\n');
					}
					indent(writer, indent);
				}
			}
			writer.write('}');
			return writer;
		} catch (IOException exception) {
			throw new JSONException(exception);
		}
	}
	
	public int length() {
		return this.map.size();
	}
	
	private Set<Map.Entry<String, Object>> entrySet() {
		return this.map.entrySet();
	}
	
	private static String quote(String string) {
		StringWriter sw = new StringWriter();
		synchronized (sw.getBuffer()) {
			try {
				return quote(string, sw).toString();
			} catch (IOException ignored) {
				// will never happen - we are writing to a string writer
				return "";
			}
		}
	}
	
	@SuppressWarnings("UnusedReturnValue")
	private static Writer writeValue(Writer writer, Object value, int indentFactor, int indent) throws JSONException,
			IOException {
		if (value == null) {
			writer.write("null");
		} else {
			if (value instanceof JSONString) {
				Object o;
				try {
					o = ((JSONString) value).toJSONString();
				} catch (Exception e) {
					throw new JSONException(e);
				}
				writer.write(o != null ? o.toString() : quote(value.toString()));
			} else {
				if (value instanceof Number) {
					// not all Numbers may match actual JSON Numbers. i.e. fractions or Imaginary
					final String numberAsString = numberToString((Number) value);
					try {
						// Use the BigDecimal constructor for its parser to validate the format.
						@SuppressWarnings("unused")
						BigDecimal testNum = new BigDecimal(numberAsString);
						// Close enough to a JSON number that we will use it unquoted
						writer.write(numberAsString);
					} catch (NumberFormatException ex) {
						// The Number value is not a valid JSON number.
						// Instead we will quote it as a string
						quote(numberAsString, writer);
					}
				} else {
					if (value instanceof Boolean) {
						writer.write(value.toString());
					} else {
						if (value instanceof Enum<?>) {
							writer.write(quote(((Enum<?>) value).name()));
						} else {
							if (value instanceof JSONObject) {
								((JSONObject) value).write(writer, indentFactor, indent);
							} else {
								if (value instanceof JSONArray) {
									((JSONArray) value).write(writer, indentFactor, indent);
								} else {
									if (value instanceof Map) {
										Map<?, ?> map = (Map<?, ?>) value;
										new JSONObject(map).write(writer, indentFactor, indent);
									} else {
										if (value instanceof Collection) {
											Collection<?> coll = (Collection<?>) value;
											new JSONArray(coll).write(writer, indentFactor, indent);
										} else {
											if (value.getClass().isArray()) {
												new JSONArray(value).write(writer, indentFactor, indent);
											} else {
												quote(value.toString(), writer);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return writer;
	}
	
	private static void indent(Writer writer, int indent) throws IOException {
		for (int i = 0; i < indent; i += 1) {
			writer.write(' ');
		}
	}
	
	private static Writer quote(String string, Writer w) throws IOException {
		if (string == null || string.length() == 0) {
			w.write("\"\"");
			return w;
		}
		
		char   b;
		char   c   = 0;
		String hhhh;
		int    i;
		int    len = string.length();
		
		w.write('"');
		for (i = 0; i < len; i += 1) {
			b = c;
			c = string.charAt(i);
			switch (c) {
				case '\\':
				case '"':
					w.write('\\');
					w.write(c);
					break;
				case '/':
					if (b == '<') {
						w.write('\\');
					}
					w.write(c);
					break;
				case '\b':
					w.write("\\b");
					break;
				case '\t':
					w.write("\\t");
					break;
				case '\n':
					w.write("\\n");
					break;
				case '\f':
					w.write("\\f");
					break;
				case '\r':
					w.write("\\r");
					break;
				default:
					if (c < ' ' || (c >= '\u0080' && c < '\u00a0')
						|| (c >= '\u2000' && c < '\u2100')) {
						w.write("\\u");
						hhhh = Integer.toHexString(c);
						w.write("0000", 0, 4 - hhhh.length());
						w.write(hhhh);
					} else {
						w.write(c);
					}
			}
		}
		w.write('"');
		return w;
	}
	
	private static String numberToString(Number number) throws JSONException {
		if (number == null) {
			throw new JSONException("Null pointer");
		}
		testValidity(number);
		
		// Shave off trailing zeros and decimal point, if possible.
		
		String string = number.toString();
		if (string.indexOf('.') > 0 && string.indexOf('e') < 0
			&& string.indexOf('E') < 0) {
			while (string.endsWith("0")) {
				string = string.substring(0, string.length() - 1);
			}
			if (string.endsWith(".")) {
				string = string.substring(0, string.length() - 1);
			}
		}
		return string;
	}
	
	private static void testValidity(Object o) throws JSONException {
		if (o != null) {
			if (o instanceof Double) {
				if (((Double) o).isInfinite() || ((Double) o).isNaN()) {
					throw new JSONException(
							"JSON does not allow non-finite numbers.");
				}
			} else {
				if (o instanceof Float) {
					if (((Float) o).isInfinite() || ((Float) o).isNaN()) {
						throw new JSONException(
								"JSON does not allow non-finite numbers.");
					}
				}
			}
		}
	}
	
	
}
