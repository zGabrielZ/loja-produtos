create table TB_ITEM_PEDIDO(
   ID bigserial primary key,
   QUANTIDADE bigserial not null,
   ID_PRODUTO bigserial not null,
   ID_PEDIDO bigserial not null
);

ALTER TABLE TB_ITEM_PEDIDO ADD FOREIGN KEY (ID_PRODUTO) references TB_PRODUTO(ID);
ALTER TABLE TB_ITEM_PEDIDO ADD FOREIGN KEY (ID_PEDIDO) references TB_PEDIDO(ID);