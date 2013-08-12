package gui.translator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import translate.AbstractTranslator;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)

public @interface Translator {
	
	/**
	 * Eindeutiger Name fuer den Uebersetzer
	 * @return
	 */
    public String name();
    
    /**
     * Die Uebersetzerklasse
     * @return
     */
    public Class<? extends AbstractTranslator> translator();
}
