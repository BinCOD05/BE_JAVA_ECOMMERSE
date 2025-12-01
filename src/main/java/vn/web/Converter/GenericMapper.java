package vn.web.Converter;


//E : Enitty , D: Request , R: Response
public interface GenericMapper<E , D ,  R> {

//    ENTITY -> RESPONSE
    R toDTOResponse(E entity);

    E toEntity(D request);

}
