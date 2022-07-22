package com.ssafy.gumid101.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_img")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageFileEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "img_seq")
	private Long img_seq;

	@Column(name = "img_original_name")
	private String img_original_name;

	@Column(name = "img_saved_name")
	private String img_saved_name;

	@Column(name = "img_saved_path")
	private String img_saved_path;

	@Column(name = "img_reg_time")
	private LocalDateTime img_reg_time;
}
