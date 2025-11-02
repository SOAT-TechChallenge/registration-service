# language: pt
@CreateProduct
Funcionalidade: Criar Produto
  Como um atendente
  Eu quero criar um novo produto
  Para que eu possa cadastrá-lo no sistema

  @sucesso
  Cenário: Criar produto com sucesso quando o nome é único
    Dado que não existe produto com o nome "Hambúrguer de Bacon"
    Quando eu criar um produto com os seguintes dados:
      | nome                | descrição                                   | preço | categoria | status      | imagem          |
      | Hambúrguer de Bacon | Delicioso hambúrguer com bacon crocante    | 35.50 | LANCHE    | DISPONIVEL  | bacon_burger.png |
    Então o produto deve ser criado com sucesso
    E o produto deve ter o nome "Hambúrguer de Bacon"
    E o produto deve ter a descrição "Delicioso hambúrguer com bacon crocante"
    E o produto deve ter o preço "35.50"
    E o produto deve ter a categoria "LANCHE"
    E o produto deve ter o status "DISPONIVEL"
    E o produto deve ter a imagem "bacon_burger.png"
    E o gateway deve verificar a unicidade do nome antes de salvar
    E o gateway deve salvar o produto

  @falha
  Cenário: Falha ao criar produto quando o nome já existe
    Dado que já existe um produto com o nome "Hambúrguer de Bacon"
    Quando eu tentar criar um produto com o nome "Hambúrguer de Bacon"
    Então deve ser lançada uma exceção do tipo NameAlreadyRegisteredException
    E a mensagem da exceção deve conter "Hambúrguer de Bacon"
    E o produto não deve ser salvo

