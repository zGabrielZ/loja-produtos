create table TB_PERFIL(
   ID bigserial primary key,
   DESCRICAO varchar(255) not null,
   AUTORIEDADE varchar(255) not null
);

alter table TB_PERFIL add constraint CK_PERFIL_AUTORIEDADE unique(AUTORIEDADE);