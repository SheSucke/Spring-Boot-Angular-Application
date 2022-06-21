/*
 * LocalDateTimeAdapter
 *
 * 0.1
 *
 * Author: M. Halamka
 */
package eu.profinit.stm.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;

/**
 * Adapter for marshalling LocalDateTime to xml representation
 */
public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {
    /**
     * Returns LocalDateTime format of date and time from String format
     *
     * @param v date in String format
     * @return date in LocalDateTime format
     */
    public LocalDateTime unmarshal(String v) {
        return LocalDateTime.parse(v);
    }

    /**
     * Returns String format of date and time from LocalDateTime format
     *
     * @param v date in LocalDateTime format
     * @return date in String format
     */
    public String marshal(LocalDateTime v) {
        return v.toString();
    }
}
