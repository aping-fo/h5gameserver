package com.game.util;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.Map.Entry;

/**
 * 配置文件规范 数值类型请用 基本类型int，不用包装类如Integer等 一维数值格式 a1,a2,a3 二维数组格式 [[a1,a2],[a3,a4]]
 * Map格式 key1:val1;key2:val2
 */

public final class GameData {
    private static Logger logger = Logger.getLogger(GameData.class);
    private static Map<String, Map<Integer, Object>> data = new HashMap<String, Map<Integer, Object>>();

    // 获取单个配置
    @SuppressWarnings("unchecked")
    public static <T> T getConfig(Class<T> t, int id) {
        return (T) (data.get(t.getSimpleName()).get(id));
    }

    // 获取全部配置
    public static Collection<Object> getConfigs(Class<?> t) {
        return data.get(t.getSimpleName()).values();
    }

    // 加载配置
    public static void loadConfigData() throws Exception {

        List<Class<?>> configClasses = loadClassByPackageName("com.game.data");

        for (Class<?> c : configClasses) {
            if (c.getSimpleName().equals("Response")) {
                continue;
            }
            String field = null;// 报错时跟踪
            String value = null;
            try {
                Map<Integer, Map<String, String>> configDatas = loadConfigFile(c.getSimpleName());
                for (Entry<Integer, Map<String, String>> entry : configDatas.entrySet()) {
                    int id = entry.getKey();

                    Object o = c.newInstance();
                    for (Entry<String, String> attr : entry.getValue().entrySet()) {
                        String prop = attr.getKey();
                        value = attr.getValue();

                        // 查找同名的属性
                        for (Field f : c.getFields()) {

                            if (f.getName().equals(prop)) {
                                field = prop;
                                Object fieldVal = null;

                                if (f.getType() == HashMap.class || f.getType() == Map.class) {
                                    fieldVal = parseMap(f.getGenericType(), value);
                                } else {
                                    fieldVal = getValue(f.getType(), value);
                                }
                                f.set(o, fieldVal);
                                break;
                            }
                        }
                    }
                    addConfig(c.getSimpleName(), id, o);
                }
                logger.info("Load Config " + c.getSimpleName());
            } catch (Exception e) {
                logger.error("Load Config Err:" + c.getSimpleName() + "." + field + " val:" + value, e);
                throw e;
            }

        }
    }

    //增加cfg
    private static void addConfig(String key, int id, Object config) {
        Map<Integer, Object> configs = data.get(key);
        if (configs == null) {
            configs = new HashMap<Integer, Object>();
            data.put(key, configs);
        }
        configs.put(id, config);
    }

    //加载类信息
    private static List<Class<?>> loadClassByPackageName(String packageName) {
        List<Class<?>> classes = new LinkedList<Class<?>>();
        try {
            String dirPath = packageName.replace(".", "/");
            Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(dirPath);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    File[] classFiles = new File(filePath).listFiles();

                    for (File classFile : classFiles) {
                        if (classFile.getName().indexOf(".class") < 0) {
                            continue;
                        }
                        String className = classFile.getName().substring(0, classFile.getName().length() - 6);
                        Class<?> clazz = Thread.currentThread().getContextClassLoader()
                                .loadClass(packageName + '.' + className);
                        classes.add(clazz);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("loadClassByPackageName", e);
        }
        return classes;
    }

    //加载配置文件
    private static Map<Integer, Map<String, String>> loadConfigFile(String name) throws Exception {

        Map<Integer, Map<String, String>> configDatas = new HashMap<Integer, Map<String, String>>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = factory.newDocumentBuilder();

        String path = System.getProperty("dataPath").concat(File.separator).concat(name).concat(".xml");
        Document doc = db.parse(path);
        Element root = doc.getDocumentElement();

        NodeList nodes = root.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node data = nodes.item(i);
            if (data.getNodeType() == Node.ELEMENT_NODE) {
                Map<String, String> item = new HashMap<String, String>();
                int key = 0;
                NodeList contents = data.getChildNodes();
                for (int j = 0; j < contents.getLength(); j++) {
                    Node n = contents.item(j);
                    if (n.getNodeType() == Node.ELEMENT_NODE) {
                        if (n.getNodeName().startsWith("id")) {
                            key = Integer.valueOf(n.getTextContent().trim());
                        }
                        item.put(n.getNodeName().trim(), n.getTextContent().trim());
                    }
                }
                configDatas.put(key, item);
            }
        }
        return configDatas;
    }

    //类型转换
    private static Object getValue(Class<?> type, String value) throws Exception {
        if (type == String.class) {
            return value;
        }
        if (type == int.class || type == Integer.class) {
            if (value == null || value.trim().isEmpty()) {
                return 0;
            }
            return Integer.valueOf(value);
        }
        if (type == float.class || type == Float.class) {
            if (value == null || value.trim().isEmpty()) {
                return 0f;
            }
            return Float.valueOf(value);
        }
        if (type == double.class || type == Double.class) {
            if (value == null || value.trim().isEmpty()) {
                return 0d;
            }
            return Double.valueOf(value);
        }
        if (type == long.class || type == Long.class) {
            if (value == null || value.trim().isEmpty()) {
                return 0l;
            }
            return Long.valueOf(value);
        }
        if (type == boolean.class || type == Boolean.class) {
            if (value.equals("1") || value.equalsIgnoreCase("true")) {
                return true;
            } else if (value.equals("0") || value.equalsIgnoreCase("false")) {
                return false;
            } else {
                return Boolean.parseBoolean(value);
            }
        }
        if (type == int[].class) {
            return parseArr(int.class, value);
        }
        if (type == int[][].class) {
            return parse2Arr(int.class, value);
        }
        if (type == float[].class) {
            return parseArr(float.class, value);
        }
        if (type == float[][].class) {
            return parse2Arr(float.class, value);
        }
        if (type == double[].class) {
            return parseArr(double.class, value);
        }
        if (type == double[][].class) {
            return parse2Arr(double.class, value);
        }
        if (type == long[].class) {
            return parseArr(long.class, value);
        }
        if (type == long[][].class) {
            return parse2Arr(long.class, value);
        }
        if (type == String[].class) {
            return parseArr(String.class, value);
        }
        if (type == String[][].class) {
            return parse2Arr(String.class, value);
        }

        return null;
    }

    //一维数组解析
    private static Object parseArr(Class<?> type, String value) throws Exception {
        if (value == null || value.isEmpty()) {
            return null;
        }
        if (value.startsWith("[")) {
            value = value.substring(1, value.length() - 1);
        }
        String[] items = value.split(",");
        Object result = Array.newInstance(type, items.length);
        for (int i = 0; i < items.length; i++) {
            items[i] = items[i].trim();
            if (type == int.class) {
                Array.set(result, i, Integer.valueOf(items[i]));
            } else if (type == float.class) {
                Array.set(result, i, Float.valueOf(items[i]));
            } else if (type == double.class) {
                Array.set(result, i, Double.valueOf(items[i]));
            } else if (type == String.class) {
                Array.set(result, i, items[i]);
            }
        }
        return result;
    }

    //二维数组解析
    private static Object parse2Arr(Class<?> type, String value) throws Exception {
        if (value == null || value.isEmpty()) {
            return null;
        }
        String[] items = value.substring(2, value.length() - 2).split("\\],\\[");
        Object result = Array.newInstance(type, new int[]{items.length, 0});
        for (int i = 0; i < items.length; i++) {
            Array.set(result, i, parseArr(type, items[i]));
        }
        return result;

    }

    //特殊类型解析map
    private static Object parseMap(Type type, String value) throws Exception {
        if (value == null || value.isEmpty()) {
            return null;
        }
        ParameterizedType pt = (ParameterizedType) type;
        Type[] arguments = pt.getActualTypeArguments();
        Class<?> params[] = new Class<?>[]{(Class<?>) arguments[0], (Class<?>) arguments[1]};
        Object map = HashMap.class.newInstance();

        String[] items = value.split(";");
        for (String item : items) {
            String entry[] = item.split("\\:");
            HashMap.class.getMethod("put", new Class<?>[]{Object.class, Object.class}).invoke(map,
                    getValue(params[0], entry[0]), getValue(params[1], entry[1]));
        }
        return map;
    }
}
