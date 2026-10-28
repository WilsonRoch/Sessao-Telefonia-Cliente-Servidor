# Sistema de Telefonia Cliente-Servidor

Aplicação Java que consulta sessões de telefonia por número de celular usando comunicação TCP e serialização de objetos.

## Funcionamento

1. O servidor carrega os registros do arquivo `telephony_sessions.txt`.
2. Cada registro é armazenado em uma tabela hash usando o número de celular como chave.
3. O cliente envia um número ao servidor.
4. O servidor retorna a sessão encontrada ou informa que o número não existe.

## Estrutura do projeto

### Classes:
- `src/model/Sessao.java`: representa uma sessão de telefonia e implementa serialização para comunicação de rede.
- `src/model/TabelaHash.java`: tabela com as sessões em um HashMap.
---

### Rede TCP: 
- `src/rede/ServidorTelefonia.java`: inicia o servidor TCP, carrega os dados e responde às consultas do cliente.
- `src/rede/ClienteTelefonia.java`: conecta-se ao servidor, envia o número de celular e exibe o resultado.
---
### Métodos e base de dados
- `src/utilidades/telephony_sessions.txt`: base de dados das sessões de telefonia.
- `src/utilidades/Util.java`: localiza e lê o arquivo das sessões, preenchendo o HashMap.

## Formato dos dados

Cada linha do arquivo de dados possui o seguinte formato:

```text
numero_celular;dados_da_sessao
```

Exemplo:

```text
5592914942603;{"session_id":"...","imsi":"...","bearer":"3G"}
```

## Execução

### Servidor

Execute:

```
ServidorTelefonia.java
```

O servidor utiliza:

- Host: `localhost`
- Porta padrão: `50001`
- Arquivo padrão: `telephony_sessions.txt`

### Cliente

Execute:

```
ClienteTelefonia.java
```

Em seguida insira o número que deseja encontrar.

## Resultado

Quando o número existe na base, o cliente exibe a sessão encontrada. Caso contrário, exibe:

```text
Número de celular não encontrado.
```
