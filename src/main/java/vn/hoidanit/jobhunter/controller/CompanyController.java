package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.CompanyService;

import org.springframework.data.domain.Pageable;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createNewCompany(@Valid @RequestBody Company postCompany) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.handleCreateCompany(postCompany));
    }

    @GetMapping("/companies")
    @ApiMessage("Fetch companies")
    public ResponseEntity<ResultPaginationDTO> fetchAllCompanies(
            @Filter Specification<Company> spec,
            Pageable pageable
    ) {
        return ResponseEntity.ok(this.companyService.fetchAllCompanies(spec, pageable));
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company postCompany) {
        return ResponseEntity.ok(companyService.handleUpdateCompany(postCompany));
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable long id) {
        this.companyService.handleDeleteCompany(id);
        return ResponseEntity.ok(null);
    }
}
