package com.ssafy.gumid101.entity;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_fcm_repo")
@Getter
@Entity
public class FCMEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fcm_id")
	private Long fcmId;
	private String token;
	private String title;
	private String content;
	
	@Column(name="fcm_user_seq")
	private Long userSeq; // 가상 참조
	
	private FCMEntity(String token,String title,String content,Long userSeq) {
		this.token=token;
		this.title=title;
		this.content=content;
		this.userSeq= userSeq;
	}
	
	
	public static FCMEntity of(String token,String title,String content,Long userSeq) {
		return new FCMEntity(token,title,content,userSeq) ;
	}
}
