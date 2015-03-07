package bynull.realty.common;

/**
 * Created by dionis on 3/5/15.
 */
public interface ConverterFactory<ST, TT, C extends Converter<ST, TT>> {
    C getTargetConverter(ST in);
    C getSourceConverter(TT in);
}
