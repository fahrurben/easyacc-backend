package com.kyrosoft.easyacc.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kyrosoft.easyacc.EntityNotFoundException;
import com.kyrosoft.easyacc.model.Account;
import com.kyrosoft.easyacc.model.Move;
import com.kyrosoft.easyacc.model.MoveLine;
import com.kyrosoft.easyacc.model.criteria.MoveCriteriaSpec;
import com.kyrosoft.easyacc.service.MoveService;

@RestController
@RequestMapping("api/moves")
public class MoveController {
	
	private MoveService service;
	
	public MoveController(MoveService moveService) {
		this.service = moveService;
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
    public Move create(@RequestBody Move move) {
		
		if (move.getLines() != null) {
			for (MoveLine line: move.getLines()) {
				line.setMove(move);
			}
		}
		
		return service.create(move);
    }
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Move get(@PathVariable Long id) throws EntityNotFoundException {
		
		return service.get(id);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public Move update(@PathVariable Long id, @RequestBody Move move) 
			throws Exception {
		
		return service.update(id, move);
	}
	
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public List<Move> find(
			HttpServletResponse response,
			@RequestBody MoveCriteriaSpec searchCriteria,
			@RequestParam String sortBy,
			@RequestParam String sortOrder,
			@RequestParam Integer page, 
			@RequestParam Integer size
			) {
		
		Sort sort = new Sort(sortOrder.equals("Asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
		Pageable pageable = new PageRequest(page - 1, size, sort);
		
		Page<Move> pageCategories = service.find(searchCriteria, pageable);
		
		response.addHeader("total", Long.toString(pageCategories.getTotalElements()));
		response.addHeader("total-page", Integer.toString(pageCategories.getTotalPages()));
		return pageCategories.getContent();
	}
}
