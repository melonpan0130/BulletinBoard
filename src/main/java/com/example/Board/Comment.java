package com.example.Board;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(exclude="article")
@Setter
@Getter
public class Comment {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer cid;
	
	@Column(nullable=false)
	@NonNull
	@NotBlank
	private String writer;
	
	@Column(nullable=false)
	@NonNull
	private String content;
	
	@ManyToOne
	@JoinColumn(name="article_id")
	private Article article;
}