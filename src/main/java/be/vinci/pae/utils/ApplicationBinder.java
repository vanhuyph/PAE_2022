package be.vinci.pae.utils;

import be.vinci.pae.business.domaine.DomaineFactory;
import be.vinci.pae.business.domaine.DomaineFactoryImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

@Provider
public class ApplicationBinder extends AbstractBinder {

  @Override
  protected void configure() {
    bind(DomaineFactoryImpl.class).to(DomaineFactory.class).in(Singleton.class);
  }
}
