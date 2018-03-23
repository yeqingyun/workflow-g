/**
 * @(#) HtmlUtil.java Created on 2014年12月5日
 *
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.htmlparser;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * The class <code>HtmlUtil</code>
 *
 * @author lipw
 * @version 1.0
 */
public class HTMLParseUtil {  
    /** 
     * 解析具有某类属性值的标签列表 
     * @see 定义泛型--><code><T extends TagNode></code> 
     * @see 使用泛型--><code>List<T>,Class<T></code> 
     * @see 这里定义了一个类型必须是某种TagNode的泛型T,并且返回的List也必须是泛型T 
     * @see 而Class<T>的意思是传给tagType的是什么类型,所以返回给List<T>的就是什么类型 
     * @see 比如传进来的是org.htmlparser.tags.MetaTag.class,那么返回的就是List<MetaTag> 
     * @param inputHTML      被解析的HTML文本 
     * @param tagType        标签的类型,内部类使用故final 
     * @param attributeName  待解析的属性名,内部类使用故final 
     * @param attributeValue 待解析的属性值,内部类使用故final 
     */  
    @SuppressWarnings({ "unchecked", "serial" })  
    public static <T extends TagNode> List<T> parseTags(String inputHTML, final Class<T> tagType, final String attributeName, final String attributeValue){  
        //创建一个HTML解析器  
        Parser parser = new Parser();  
        NodeList tagList = null;  
        try {  
            parser.setInputHTML(inputHTML);  
            //它会自动检测文件内部<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>  
            //parser.setEncoding("UTF-8");  
            tagList = parser.parse(  
                new NodeFilter(){  
                    @Override  
                    public boolean accept(Node node){  
                        //这里不需要if(node instanceof TagNode),因为上面已经定义了TagNode类型的泛型T  
                        if(node.getClass()==tagType){  
                            //Node类型的实现类中只有TagNode才能getAttribute()获取属性值,所以要将之打回原形  
                            T t = (T)node;  
                            //若传入的属性名是null,则认为是不需要查找指定属性值的标签,而是单纯的查找某类型T的标签  
                            //if(null == attributeName){  
                            //  return true;  
                            //}  
                            if(null!=attributeValue && attributeValue.equals(t.getAttribute(attributeName))){  
                                return true;  
                            }  
                        }  
                        return false;  
                    }  
                }  
            );  
        } catch (ParserException e) {  
            System.out.println("解析HTML文本时发生异常:" + e.getMessage());  
        }  
        List<T> tags = new ArrayList<T>();  
        for(int i=0; i<tagList.size(); i++){  
            T t = (T)tagList.elementAt(i); //提取真实tag  
            tags.add(t);  
        }  
        return tags;  
    }  
      
      
    /** 
     * 解析属性值唯一的标签 
     * @see 和上面那个方法差不多,传给Class<T>的是什么类型,那么返回的就是什么类型 
     */  
    public static <T extends TagNode> T parseTag(String inputHTML, final Class<T> tagType, final String attributeName, final String attributeValue){  
        List<T> tagList = parseTags(inputHTML, tagType, attributeName, attributeValue);  
        if(null!=tagList && tagList.size()>0){  
            return tagList.get(0);  
        }else{  
            return null;  
        }  
    }  
}  
