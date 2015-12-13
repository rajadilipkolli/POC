package com.example.poc.soap.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(targetNamespace = "http://example.com/", name = "SOAPWebService")
public interface Webservice {
    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "sayHello", targetNamespace = "http://service.example.com/", className = "com.example.poc.soap.service.SayHello")
    @WebMethod(action = "urn:SayHello")
    @ResponseWrapper(localName = "sayHelloResponse", targetNamespace = "http://service.example.com/", className = "com.example.poc.soap.service.SayHelloResponse")
    public String sayHello(@WebParam(name = "myname", targetNamespace = "") String myname);
}
