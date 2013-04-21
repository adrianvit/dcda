package com.team314.dcda.local.db;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "orders")
@XmlRootElement(name = "order")
public class Order {

}
