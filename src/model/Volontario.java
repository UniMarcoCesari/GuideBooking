package model;

import java.io.Serializable;

public class Volontario implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String nome;

    public Volontario(String nome)
    {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
