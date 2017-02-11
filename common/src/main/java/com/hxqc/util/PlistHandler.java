/**
 * author 胡俊杰
 * Todo
 */
package com.hxqc.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * 使用方法 解析plist文件 SAXParserFactory factorys =
 * SAXParserFactory.newInstance(); SAXParser saxparser; saxparser =
 * factorys.newSAXParser(); PlistHandler plistHandler = new
 * PlistHandler(); saxparser.parse(is, plistHandler); HashMap<String,
 * Object> hash = plistHandler.getMapResult(); ArrayList<Object> array
 * = (ArrayList<Object>) plistHandler.getArrayResult();
 */
public class PlistHandler extends DefaultHandler {

    Stack< Object > stack = new Stack< Object >();
    private boolean isRootElement = false;
    private boolean keyElementBegin = false;
    private String key;
    private boolean valueElementBegin = false;

    private Object root;

    @SuppressWarnings("unchecked")
    public HashMap< String, Object > getMapResult() {
        return (HashMap< String, Object >) root;
    }

    @SuppressWarnings("unchecked")
    public List< Object > getArrayResult() {
        return (List< Object >) root;
    }

    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void endDocument() throws SAXException {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("plist".equals(qName)) {
            isRootElement = true;
        }
        if ("dict".equals(qName)) {
            if (isRootElement) {
                stack.push(new HashMap< String, Object >());// 压栈
                isRootElement = !isRootElement;
            } else {
                Object object = stack.peek();
                if (object instanceof ArrayList) {
                    ((ArrayList< Object >) object).add(new HashMap<>());
                } else if (object instanceof HashMap) {
                    ((HashMap< String, Object >) object).put(key, new HashMap<>());
                }
                stack.push(new HashMap<>());
            }
        }

        if ("key".equals(qName)) {
            keyElementBegin = true;
        }
        if ("true".equals(qName)) {
            HashMap< String, Object > parent = (HashMap< String, Object >) stack.peek();
            parent.put(key, true);
        }
        if ("false".equals(qName)) {
            HashMap< String, Object > parent = (HashMap< String, Object >) stack.peek();
            parent.put(key, false);
        }
        if ("array".equals(qName)) {
            if (isRootElement) {
//                ArrayList< Object > obj = new ArrayList< Object >();
                stack.push(new ArrayList<>());
                isRootElement = !isRootElement;
            } else {
                HashMap< String, Object > parent = (HashMap< String, Object >) stack.peek();
                ArrayList< Object > obj = new ArrayList<  >();
                stack.push(obj);
                parent.put(key, obj);
            }
        }
        if ("string".equals(qName)) {
            valueElementBegin = true;
        }
    }

    /*
     * 字符串解析(non-Javadoc)
     *
     * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int,
     * int)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (length > 0) {
            if (keyElementBegin) {
                key = new String(ch, start, length);
            }
            if (valueElementBegin) {
                if (HashMap.class.equals(stack.peek().getClass())) {
                    HashMap< String, Object > parent = (HashMap< String, Object >) stack.peek();
                    String value = new String(ch, start, length);
                    parent.put(key, value);
                } else if (ArrayList.class.equals(stack.peek().getClass())) {
                    ArrayList< Object > parent = (ArrayList< Object >) stack.peek();
                    String value = new String(ch, start, length);
                    parent.add(value);
                }
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
//        if ("plist".equals(qName)) {
//        }
        if ("key".equals(qName)) {
            keyElementBegin = false;
        }
        if ("string".equals(qName)) {
            valueElementBegin = false;
        }
        if ("array".equals(qName)) {
            root = stack.pop();
        }
        if ("dict".equals(qName)) {
            root = stack.pop();
        }
    }
}
