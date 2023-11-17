package br.facens.aula.aulafinalautenticacaofirebase;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewUserInfoActivity extends AppCompatActivity {

    private EditText cpfEditText;
    private EditText nomeEditText;
    private EditText idadeEditText;
    private Button saveButton;
    private Button searchButton;
    private Button removeButton;
    private TextView resultTextView;
    private TextView displayTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_info);

        cpfEditText = findViewById(R.id.cpfEditText);
        nomeEditText = findViewById(R.id.nomeEditText);
        idadeEditText = findViewById(R.id.idadeEditText);
        saveButton = findViewById(R.id.saveButton);
        searchButton = findViewById(R.id.searchButton);
        removeButton = findViewById(R.id.removeButton);
        resultTextView = findViewById(R.id.resultTextView);
        displayTextView = findViewById(R.id.displayTextView);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInfo();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchUser();
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeUser();
            }
        });
    }

    private void saveUserInfo() {
        String cpf = cpfEditText.getText().toString();
        String nome = nomeEditText.getText().toString();
        String idade = idadeEditText.getText().toString();

        /*
            Este método estático é usado para obter uma instância única do Firebase Realtime Database
            para o seu aplicativo. O Firebase gerencia automaticamente as configurações e autenticação.
            Portanto, getInstance() recupera uma referência para o banco de dados em que você planeja
            armazenar os dados.
        */
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        /*
            Este é um método da instância do banco de dados que recebe uma string como argumento. Ele
            retorna uma referência ao local especificado no banco de dados.

            O parâmetro passado para o método getReference() especifica a localização no banco de dados
            onde você deseja realizar operações. Esse parâmetro é uma string que representa o caminho
            para o local desejado no banco de dados.

            A estrutura do Firebase Realtime Database é semelhante a uma árvore de dados JSON, em que
            cada nó é representado por uma chave e contém valores ou outros nós. O parâmetro getReference()
            permite que você navegue por essa estrutura e acesse os dados em locais específicos do banco de dados.

            Neste exemplo todos os dados de usuários estão/serão armazenados como sub-nós da chave usuário.
         */
        DatabaseReference userRef = database.getReference("usuario");

        /*
             O método .child() é usado para criar ou acessar um nó filho (subnó) dentro do nó referenciado
             por userRef. Neste caso, cpf é passado como argumento para criar um subnó com o nome igual
             ao valor da variável cpf. Isso é útil quando você deseja organizar os dados de acordo
             com identificadores exclusivos, como CPF neste caso.

              Em seguida, outro método .child() é chamado para acessar um subnó dentro do nó identificado
              por cpf. Aqui, estamos acessando um subnó chamado "nome". Isso é útil para criar uma estrutura
              hierárquica de dados no banco de dados.

              .setValue(nome): Este método é usado para definir o valor associado ao nó "nome" (que é um
              subnó de cpf) como o valor da variável nome. Em outras palavras, ele armazena o valor da
              variável nome no banco de dados, associado ao nó "nome" dentro do nó cpf. O mesmo para
              "idade".
        */
        userRef.child(cpf).child("nome").setValue(nome);
        userRef.child(cpf).child("idade").setValue(idade);

        resultTextView.setText("Sucesso ao armazenar");
    }

    private void searchUser() {
        final String cpf = cpfEditText.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("usuario");

        /*
            Esta parte do código está adicionando um ouvinte de eventos ao nó cpf específico no Firebase
            Realtime Database. Isso é feito usando o método addListenerForSingleValueEvent, que é usado
            para buscar um único valor no nó especificado e escutar por mudanças nesse valor.
            A interface ValueEventListener é usada para lidar com as respostas quando os dados são recuperados.
        */

        userRef.child(cpf).addListenerForSingleValueEvent(new ValueEventListener() {

            /*
                Este método é chamado quando os dados são lidos com sucesso. O DataSnapshot contém os
                dados encontrados no caminho especificado.
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /*
                     Aqui, o código verifica se há algum dado no DataSnapshot (ou seja, se o nó com base
                     no CPF existe no banco de dados). Se exists() retornar true, significa que os dados
                     foram encontrados.
                 */
                if (dataSnapshot.exists()) {
                    String nome = dataSnapshot.child("nome").getValue(String.class);
                    String idade = dataSnapshot.child("idade").getValue(String.class);

                    displayTextView.setText("Nome: " + nome + "\nIdade: " + idade);
                    resultTextView.setText(""); // Limpa o resultTextView
                    removeButton.setVisibility(View.VISIBLE); // Mostra o botão de remoção
                } else {
                    displayTextView.setText("Não encontrado");
                    resultTextView.setText(""); // Limpa o resultTextView
                    removeButton.setVisibility(View.GONE); // Oculta o botão de remoção
                }
            }

            /*
                Este método é chamado em caso de erros na leitura de dados.
             */
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                displayTextView.setText("Erro ao buscar dados");
                resultTextView.setText(""); // Limpa o resultTextView
                removeButton.setVisibility(View.GONE);
            }
        });
    }

    private void removeUser() {
        String cpf = cpfEditText.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("usuario");

        /*
            O método remove o valor referente ao nó
         */
        userRef.child(cpf).removeValue();

        displayTextView.setText("Registro removido");
        resultTextView.setText(""); // Limpa o resultTextView
        removeButton.setVisibility(View.GONE); // Oculta o botão de remoção
    }
}
