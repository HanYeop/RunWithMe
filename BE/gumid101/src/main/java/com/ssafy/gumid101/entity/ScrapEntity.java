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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_scrap")
public class ScrapEntity {
	
	@Id
	@Column(name="scrap_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long scrapSeq;

	@Column(name="scrap_title")
	private String scrapTitle;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_seq")
	private UserEntity userEntity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "track_board_seq")
	private TrackBoardEntity trackBoardEntity;
	
}
