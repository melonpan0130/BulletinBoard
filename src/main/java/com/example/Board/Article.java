package com.example.Board;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
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
@ToString
@Setter
@Getter
public class Article {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(unique=true, nullable=false)
	@NonNull
	private String title;
	
	@Column(nullable=false)
	@NonNull
	@NotBlank
	private String writer;
	
	@Lob
	@NonNull
	private String content;
	
	// mappedBy를 이용하여 외래키 관계 생성
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY, mappedBy="article")
	private List<Comment> comments = new ArrayList<>();
}