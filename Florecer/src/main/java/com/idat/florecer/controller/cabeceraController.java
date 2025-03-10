package com.idat.florecer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idat.florecer.entity.CabeceraVenta;
import com.idat.florecer.entity.DetalleVenta;
import com.idat.florecer.entity.Producto;
import com.idat.florecer.service.ICabeceraVentaService;
import com.idat.florecer.service.IProductoService;
import com.idat.florecer.serviceR.CabeVServiceIm;
import com.idat.florecer.serviceR.DetaVServiceIm;


@CrossOrigin(origins= {"http://localhost/4200"})
@RestController
@RequestMapping("/cabecera")
public class cabeceraController {

	@Autowired
	private ICabeceraVentaService cabeceraService;
	@Autowired
	private CabeVServiceIm cabeService;
	@Autowired
	private DetaVServiceIm detaService;
	@Autowired
	private IProductoService productoService;
	
	
	//LISTAR CABECERAS DE VENTA
	@GetMapping("/cabeceras")
	public List<CabeceraVenta> listar(){
		return cabeceraService.findAll();
	}
	
	//BUSCAR CABECERA POR ID
	@GetMapping("/cabecera/{id}")
	public CabeceraVenta cabecera (@PathVariable Long id) {
		return cabeceraService.findById(id);
	}
	
	//CREAR NUEVO CABECERA
	@PostMapping("/cabeceranew")
	public CabeceraVenta cabeceraonew(@RequestBody CabeceraVenta cabecera) {
		cabeceraService.save(cabecera);
		return cabeceraService.findById(cabecera.getIdCabecera()); 
	}
	
	//CREAR NUEVO CABECERA Carrito
		@PostMapping("/cabeceraregistro/{id}")
		public CabeceraVenta cabeceraregister(@PathVariable Long id) {
			return cabeService.saveCabecera(id);
		}
	
		//BUSCAR CABECERA POR ID
		@GetMapping("/cabeceraidu/{id}")
		public CabeceraVenta getbyidUser (@PathVariable Long id) {
			return cabeService.getCabe(id);
		}
		
		@GetMapping("/consultaventa")
		public List<Object> ConsultaVenta(){
			return cabeService.ConsultaVentas();
		}
		@GetMapping("/contarventa")
		public List<Object> ContarVenta(){
			return cabeService.ContarVentas();
		}
	
	//ACTUALIZAR CABECERA
	@PutMapping("/cabeceraupdate/{id}")
	public CabeceraVenta actualizar(@RequestBody CabeceraVenta cabecera,@PathVariable Long id) {
		CabeceraVenta cabeceraActual=cabeceraService.findById(id);
		cabeceraActual.setBruto(cabecera.getBruto());
		cabeceraActual.setIgv(cabecera.getIgv());
		cabeceraActual.setNeto(cabecera.getNeto());
		cabeceraActual.setFechamat(cabecera.getFechamat());
		cabeceraActual.setEstado(cabecera.getEstado());
		cabeceraActual.setTipoPago(cabecera.getTipoPago());
		cabeceraActual.setTipoCabecera(cabecera.getTipoCabecera());
		
		cabeceraService.save(cabeceraActual);
		return cabeceraService.findById(id);
	}
	
	@PutMapping("/cabeceratipo/{id}")
	public CabeceraVenta actualizarTipo(@RequestBody CabeceraVenta cabecera,@PathVariable Long id) {
		CabeceraVenta cabeceraActual=cabeceraService.findById(id);
		cabeceraActual.setTipoCabecera("Venta");
		
		cabeceraService.save(cabeceraActual);
		return cabeceraService.findById(id);
	}
	
	//ELIMINAR CABECERA
	@DeleteMapping("/cabeceradelete/{id}")
	public void delete(@PathVariable Long id) {
		cabeceraService.eliminarCabeceraVenta(id);
	}
	
	//LISTAR CABECERA DE CARRITO DE COMPRAS
	@GetMapping("cabeceraCarrito/{idUser}")
	public CabeceraVenta carrito(@PathVariable Long idUser) {
		List<CabeceraVenta> listaCabecera=cabeceraService.findAll();
		
		CabeceraVenta cabe=new CabeceraVenta();
		for (int i=0;i<listaCabecera.size();i++) {
			if(listaCabecera.get(i).getTipoCabecera().equals("Carrito") && 
			listaCabecera.get(i).getUsuario().getIdUsuario().equals(idUser)) {
				
				 cabe=listaCabecera.get(i);
				 break;
			}
		}
		
		return cabe;
		
	}
	
	//CAMBIAR ESTADO DE CARRITO A VENTA
	@PutMapping("/venderCabecera/{idCab}")
	public void vender(@PathVariable Long idCab,@RequestBody CabeceraVenta cabecera) {
		CabeceraVenta cabe=cabeceraService.findById(idCab);
		System.out.println("====================================");
		System.out.println(cabe.getUsuario().getIdUsuario()+"       "+idCab);

		System.out.println("====================================");
		List<DetalleVenta> listaDetalle2=detaService.findByCaU(cabe.getIdCabecera());
		for(int i=0;i<listaDetalle2.size();i++) {
			Producto productoActual=productoService.findById(listaDetalle2.get(i).getProducto().getIdProducto());
			productoActual.setStock(productoActual.getStock()-listaDetalle2.get(i).getCantidad());
			productoService.save(productoActual);
			
			//cabeceraService.findById(listaDetalle.get(i).getCabecera().getIdCabecera());
			
		}
		

		cabeService.venderCabe(idCab,cabe.getUsuario().getIdUsuario());
		cabeceraregister(cabe.getUsuario().getIdUsuario());
		
		
		
	}
	
	@GetMapping("/cabeceracliente/{idUser}")
	public List<CabeceraVenta> listarCliente(@PathVariable Long idUser){
		return cabeService.findByCaU(idUser);
	}
	
	@GetMapping("/cabeceratodos")
	public List<CabeceraVenta> listarTodosC(){
		return cabeService.ListCabV();
	}
	@GetMapping("/cabeceratodosp")
	public List<CabeceraVenta> listarTodosCP(){
		return cabeService.ListCabPend();
	}
	@GetMapping("/cabeceratodosfull")
	public List<CabeceraVenta> listarTodosCfull(){
		return cabeService.ListCabTodos();
	}
	
	//ELIMINAR CABECERA
		@DeleteMapping("/cabeceraestado/{id}")
		public void deleteestado(@PathVariable Long id) {
			CabeceraVenta cabeceraActual=cabeceraService.findById(id);
			cabeceraActual.setEstado(0);
			cabeceraService.save(cabeceraActual);
		}
	
	
	
	

}
