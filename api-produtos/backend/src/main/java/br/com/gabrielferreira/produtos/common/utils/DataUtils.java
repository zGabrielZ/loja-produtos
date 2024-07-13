package br.com.gabrielferreira.produtos.common.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DataUtils {

    private DataUtils(){}

    public static final ZoneId UTC = ZoneId.of("UTC");

    public static final ZoneId FUSO_HORARIO_PADRAO_SISTEMA = ZoneId.systemDefault();

    public static final DateTimeFormatter FORMAT_DATA_HORA_MINUTO_SEGUNDO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static ZonedDateTime toFusoPadraoSistema(ZonedDateTime data){
        return data != null ? data.withZoneSameInstant(FUSO_HORARIO_PADRAO_SISTEMA) : null;
    }
}
