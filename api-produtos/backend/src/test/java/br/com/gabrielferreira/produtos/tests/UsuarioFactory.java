package br.com.gabrielferreira.produtos.tests;

import br.com.gabrielferreira.produtos.api.dto.create.PerfilCreateDTO;
import br.com.gabrielferreira.produtos.api.dto.create.UsuarioCreateDTO;
import br.com.gabrielferreira.produtos.api.dto.update.UsuarioSenhaUpdateDTO;
import br.com.gabrielferreira.produtos.api.dto.update.UsuarioUpdateDTO;
import br.com.gabrielferreira.produtos.domain.model.Usuario;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class UsuarioFactory {

    private UsuarioFactory(){}

    public static UsuarioCreateDTO criarUsuario(){
        List<PerfilCreateDTO> perfis = new ArrayList<>();
        perfis.add(new PerfilCreateDTO(1L));
        perfis.add(new PerfilCreateDTO(2L));
        perfis.add(new PerfilCreateDTO(3L));

        return UsuarioCreateDTO.builder()
                .nome("Teste")
                .email("teste@email.com.br")
                .senha("@Aa123")
                .perfis(perfis)
                .build();
    }

    public static UsuarioUpdateDTO atualizarUsuario(){
        List<PerfilCreateDTO> perfis = new ArrayList<>();
        perfis.add(new PerfilCreateDTO(1L));
        perfis.add(new PerfilCreateDTO(2L));
        perfis.add(new PerfilCreateDTO(3L));

        UsuarioUpdateDTO usuarioUpdateDTO = new UsuarioUpdateDTO();
        usuarioUpdateDTO.setNome("Teste Editado");
        usuarioUpdateDTO.setPerfis(perfis);
        return usuarioUpdateDTO;
    }

    public static UsuarioSenhaUpdateDTO atualizarSenhaUsuario(){
        return UsuarioSenhaUpdateDTO.builder()
                .antigaSenha("@Aa123")
                .novaSenha("@Aa2024")
                .build();
    }

    public static Usuario criarUsuarioModel(Long id){
        return Usuario.builder()
                .id(id)
                .nome("Teste 123")
                .email("teste@email.com")
                .senha("123")
                .pedidos(new ArrayList<>())
                .perfis(new ArrayList<>())
                .dataInclusao(ZonedDateTime.now())
                .build();
    }
}
