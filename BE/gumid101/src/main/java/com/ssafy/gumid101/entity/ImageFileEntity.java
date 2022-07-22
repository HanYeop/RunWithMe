package com.ssafy.gumid101.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t_img")
@Getter
@Setter
@Builder
public class ImageFileEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "img_seq")
	private Long imgSeq;

	@Column(nullable = false, name = "img_original_name")
	private String imgOriginalName;
	
	@Column(nullable = false, name = "img_saved_name")
	private String imgSavedName;
	
	@Column(nullable = false, name = "img_saved_path")
	private String imgSavedPath;
	
	@Column(nullable = false, name = "img_reg_time")
	private LocalDateTime imgRegTime;
}
