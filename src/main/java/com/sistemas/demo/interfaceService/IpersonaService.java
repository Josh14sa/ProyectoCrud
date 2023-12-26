package com.sistemas.demo.interfaceService;

import java.util.List;
import java.util.Optional;

import com.sistemas.demo.modelo.Persona;

public interface IpersonaService {
	
	//Declaracion de los Metodos del CRUD
	public List<Persona> listar();
	public Optional<Persona> listarId(int id);
	public int guardar(Persona p);
	public void eliminar(int id);
		

}
