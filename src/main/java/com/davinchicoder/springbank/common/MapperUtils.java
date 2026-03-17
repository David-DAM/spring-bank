package com.davinchicoder.springbank.common;

import org.mapstruct.Mapper;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.Instant;
import java.time.ZoneId;
import java.util.GregorianCalendar;

@Mapper
public interface MapperUtils {

    default Instant toInstant(XMLGregorianCalendar calendar) {
        return calendar.toGregorianCalendar().toInstant();
    }

    default XMLGregorianCalendar toGregorianCalendar(Instant time) {
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(GregorianCalendar.from(time.atZone(ZoneId.systemDefault())));
        } catch (DatatypeConfigurationException e) {
            return null;
        }
    }

}
