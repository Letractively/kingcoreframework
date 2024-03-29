package com.kingcore.framework.upload ;


import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;


public class Upload
{
  private ServletRequest request;
  private ServletResponse response;
  private ServletConfig config;
  ServletInputStream DATA;
  int FormSize;
  File f1;
  FileOutputStream os;
  DataInputStream is;
  String filename;
  byte[] b;
  byte t;
  boolean flag = false;
  public Upload() {}

  public void initialize(ServletConfig config, HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
    this.request = request;
    this.response = response;
    this.config = config;
    DATA = request.getInputStream();
    FormSize = request.getContentLength();
  }

  public void initialize(PageContext pageContext) throws IOException {
    request = pageContext.getRequest();
    response = pageContext.getResponse();
    config = pageContext.getServletConfig();
    DATA = request.getInputStream();
    FormSize = request.getContentLength();
  }

  public boolean setFilename(String s) {
    try {
      File f1 = new File(s);
      os = new FileOutputStream(f1);
    }
    catch (IOException e) {
      return (false);
    }
    return (true);
  }

  public void getByte() {
    int i = 0;
    try {
      is = new DataInputStream(DATA);
      b = new byte[FormSize];

      while (true) {
        try {
          t = is.readByte();
          b[i] = t;
          i++;
        }
        catch (EOFException e) {
          break;
        }
      }
      is.close();
    }
    catch (IOException e) {}
  }

  public boolean save() {
    int i = 0, start1 = 0, start2 = 0;
    String temp = "\"";
    if (!flag) {
      getByte();
      flag = true;
    }
    try {
      temp = new String(b, "ISO8859_1");
    }
    catch (UnsupportedEncodingException e) {
      return (false);
    }

    start1 = temp.indexOf("image/");
    temp = temp.substring(start1);

    start1 = temp.indexOf("\\r\\n\\r\\n\"");

    temp = temp.substring(start1 + 4);
    start2 = temp.indexOf(";\\r\\n\"");
    if (start2 != -1) {
      temp = temp.substring(0, start2);
    }
    try {
      byte[] img = temp.getBytes("ISO8859_1");
                                 for (i = 0; i < img.length; i++) {
        os.write(img[i]);
      }
      os.close() ;
    }
    catch (IOException e) {
      return (false);
    }

    return (true);

  }
  }
