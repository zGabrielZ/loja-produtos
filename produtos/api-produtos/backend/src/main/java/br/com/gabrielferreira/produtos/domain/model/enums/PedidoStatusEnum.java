package br.com.gabrielferreira.produtos.domain.model.enums;

public enum PedidoStatusEnum {

    ABERTO,
    FINALIZADO,
    CANCELADO;

    public static boolean isFinalizado(PedidoStatusEnum pedidoStatusEnum){
        return PedidoStatusEnum.FINALIZADO.equals(pedidoStatusEnum);
    }

    public static boolean isCancelado(PedidoStatusEnum pedidoStatusEnum){
        return PedidoStatusEnum.CANCELADO.equals(pedidoStatusEnum);
    }
}
