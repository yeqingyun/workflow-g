package com.gionee.gniflow.web.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.gnif.dto.MessageDto;
import com.gionee.gnif.dto.PageDto;
import com.gionee.gnif.dto.QueryMap;
import com.gionee.gnif.dto.TreeDto;
import com.gionee.gnif.web.util.EntryFactory;
import com.gionee.gniflow.biz.model.Category;
import com.gionee.gniflow.biz.service.CategoryService;

@Controller
@RequestMapping("/category")
public class CategoryController {
	
	@Autowired
	CategoryService categoryService;
	
	@RequestMapping("/add.json")
	public ResponseEntity<MessageDto> add(Category category) {
		return EntryFactory.create(categoryService.add(category));
	}
	
	@RequestMapping("/modify.json")
	public ResponseEntity<MessageDto> modify(Category category) {
		return EntryFactory.create(categoryService.modify(category));
	}
	
	@RequestMapping("/remove.json")
	public ResponseEntity<MessageDto> remove(Long[] ids) {
		return EntryFactory.create(categoryService.remove(Arrays.asList(ids)));
	}
	
	@RequestMapping("/get.json")
	@ResponseBody
	public Category get(Long id) {
		return categoryService.get(id);
	}
	
	@RequestMapping("/find.json")
	@ResponseBody
	public PageDto<Category> find(QueryMap queryMap) {
		return categoryService.find(queryMap);
	}
	
	@RequestMapping("/all.json")
	@ResponseBody
	public List<Category> findAll(QueryMap queryMap) {
		return categoryService.findAll(queryMap);
	}
	
	@RequestMapping("/trees.json")
	@ResponseBody
	public List<TreeDto> getTree(Long id) {
		return categoryService.getTree(id, true);
	}
	
}
