package com.sistemas.demo.controler;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lowagie.text.DocumentException;
import com.sistemas.demo.interfaceService.IpersonaService;
import com.sistemas.demo.modelo.Persona;
import com.sistemas.demo.reportes.PersonasExporterExcel;
import com.sistemas.demo.reportes.PersonasExporterPDF;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping
public class Controlador {

	@Autowired
	private IpersonaService service;
	
	@GetMapping("/listar")
	public String listar(Model model) {
		List<Persona> personas = service.listar();
		model.addAttribute("personas", personas);
		return "index";
	}
	
	@GetMapping("/editar/{id}")
	public String editar(@PathVariable int id, Model model) {
		Optional<Persona> persona = service.listarId(id);
		model.addAttribute("persona",persona);
		return "form";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(Model model, @PathVariable int id) {
		service.eliminar(id);
		return "redirect:/listar";
	}
	
	@GetMapping("/new")
	public String agregar(Model model) {
		model.addAttribute("persona", new Persona());
		return "form";
	}
	
	@PostMapping("/save")
	public String guardar(@Validated Persona p, Model model) {
		service.guardar(p);
		return "redirect:/listar";
	}
	
	@GetMapping("/exportarPDF")
	public void exportarListadoDePersonasEnPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("aplicattion/pdf");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM_dd_HH:mm:ss");
		String fechaActual= dateFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment;filename = Personas_"+ fechaActual + ".pdf";
		
		response.setHeader(cabecera, valor);
		List<Persona> personas = service.listar();
		
		PersonasExporterPDF exporter = new PersonasExporterPDF(personas);
		exporter.exportar(response);
	}
	
	@GetMapping("/exportarExcel")
	public void exportarListadoDePersonasEnExcel(HttpServletResponse response) throws IOException {
		response.setContentType("aplicattion/octet-stream");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM_dd_HH:mm:ss");
		String fechaActual= dateFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment;filename = Personas_"+ fechaActual + ".xlsx";
		
		response.setHeader(cabecera, valor);
		List<Persona> personas = service.listar();
		
		PersonasExporterExcel exporter = new PersonasExporterExcel(personas);
		exporter.exportar(response);
	}
}
