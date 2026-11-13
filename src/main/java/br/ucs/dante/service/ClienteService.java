
package br.ucs.dante.service;

import br.ucs.dante.model.Cliente;
import br.ucs.dante.repository.ClienteRepository;

import java.sql.SQLException;
import java.util.List;

public class ClienteService {

    private final ClienteRepository repository;

    // Permite testar as validacoes isoladamente.
    public ClienteService() {
        this.repository = null;
    }

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public void validar(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente nao informado.");
        }

        if (cliente.getCodigo() != null && cliente.getCodigo() <= 0) {
            throw new IllegalArgumentException("O codigo deve ser positivo.");
        }

        if (cliente.getNome() == null || cliente.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do cliente é obrigatório.");
        }

        if (cliente.getNome().length() > 50) {
            throw new IllegalArgumentException(
                "O nome deve ter no máximo 50 caracteres."
            );
        }

        if (cliente.getCpf() != null && cliente.getCpf().length() > 11) {
            throw new IllegalArgumentException(
                "O CPF deve ter no máximo 11 caracteres."
            );
        }

        if (cliente.getTelefone() != null &&
            cliente.getTelefone().length() > 15) {
            throw new IllegalArgumentException(
                "O telefone deve ter no máximo 15 caracteres."
            );
        }
    }

    public Cliente salvar(Cliente cliente) throws SQLException {
        validar(cliente);
        return repository.salvar(cliente);
    }

    public List<Cliente> listar() throws SQLException {
        return repository.listar();
    }

    public Cliente buscarPorCodigo(int codigo) throws SQLException {
        if (codigo <= 0) {
            throw new IllegalArgumentException("Código inválido.");
        }
        return repository.buscarPorCodigo(codigo);
    }

    public boolean excluir(int codigo) throws SQLException {
        return repository.excluir(codigo);
    }
}
