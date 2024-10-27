package com.idat.florecer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idat.florecer.dao.ITipoPagoDao;
import com.idat.florecer.entity.TipoPago;
@Service
public class ITipoPagoServiceImpl implements ITipoPagoService {

	@Autowired
	private ITipoPagoDao tipoPagoDao;
	
	@Transactional(readOnly=true)
	
	@Override
	public List<TipoPago> findAll() {
		
		return (List<TipoPago>)tipoPagoDao.findAll();
	}
	@Transactional
	@Override
	public void save(TipoPago tipoPago) {
		tipoPagoDao.save(tipoPago);
		
	}
	@Transactional
	@Override
	public TipoPago editarTipoPago(Long id) {
		// TODO Auto-generated method stub
		return tipoPagoDao.findById(id).orElse(null);
	}
	@Transactional
	@Override
	public void eliminarTipoPago(Long id) {
		tipoPagoDao.deleteById(id);
		
	}
	@Override
	public TipoPago findById(Long id) {
		return tipoPagoDao.findById(id).orElse(null);
	}

}
