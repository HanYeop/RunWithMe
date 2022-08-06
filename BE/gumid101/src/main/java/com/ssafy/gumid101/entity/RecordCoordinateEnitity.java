package com.ssafy.gumid101.entity;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.geo.Point;

import lombok.Setter;

@Setter
@Entity
@Table(name = "t_record_coordinate")
public class RecordCoordinateEnitity {

	
	@Id
	@Column(name="coordinate_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long coordinateSeq;
	

	//@Column(name="t_run_record_seq")
	//private Long runRecordSeq;
	
	//@Column(name = "lineString", columnDefinition = "LINESTRING")
	//private Geometry geometry;
	
	@JoinColumn(name = "run_record_seq")
	@ManyToOne(fetch = FetchType.LAZY)
	private RunRecordEntity runRecord;
	
	@Column
	private double latitude;
	@Column
	private double longitude;
}
