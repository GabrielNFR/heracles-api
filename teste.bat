@echo off
echo ========================================
echo   HERACLES API - TESTES
echo ========================================
echo.

echo [1] LISTAR TREINOS
curl -s http://localhost:8080/treinos
echo.
echo.

echo [2] BUSCAR PEITO (ID=1)
curl -s http://localhost:8080/treinos/1
echo.
echo.

echo [3] BUSCAR ID INEXISTENTE (999)
curl -s http://localhost:8080/treinos/999
echo.
echo.

echo [4] CRIAR TREINO "Ombros"
curl -s -X POST http://localhost:8080/treinos -H "Content-Type: application/json" -d "{\"nome\":\"Ombros\",\"exercicios\":[{\"nome\":\"Elevacao lateral\"},{\"nome\":\"Desenvolvimento\"}]}"
echo.
echo.

echo [5] CRIAR TREINO COM NOME VAZIO (validacao)
curl -s -X POST http://localhost:8080/treinos -H "Content-Type: application/json" -d "{\"nome\":\"\",\"exercicios\":[{\"nome\":\"Teste\"}]}"
echo.
echo.

echo [6] CRIAR TREINO SEM EXERCICIOS (validacao)
curl -s -X POST http://localhost:8080/treinos -H "Content-Type: application/json" -d "{\"nome\":\"Braco\",\"exercicios\":[]}"
echo.
echo.

echo [7] EXECUCOES DO PEITO
curl -s http://localhost:8080/treinos/1/execucoes
echo.
echo.

echo [8] EXECUCOES DE PERNAS (vazio)
curl -s http://localhost:8080/treinos/2/execucoes
echo.
echo.

echo [9] CRIAR EXECUCAO EM PERNAS
curl -s -X POST http://localhost:8080/treinos/2/execucoes -H "Content-Type: application/json" -d "{\"dataHora\":\"2026-06-29T14:30:00\",\"exercicios\":[{\"exercicioId\":4,\"series\":4,\"repeticoes\":\"8\",\"carga\":80},{\"exercicioId\":5,\"series\":3,\"repeticoes\":\"12\",\"carga\":100}]}"
echo.
echo.

echo [10] CRIAR EXECUCAO SEM EXERCICIOS (validacao)
curl -s -X POST http://localhost:8080/treinos/1/execucoes -H "Content-Type: application/json" -d "{\"dataHora\":\"2026-06-29T14:30:00\",\"exercicios\":[]}"
echo.
echo.

echo [11] CRIAR EXECUCAO EM TREINO INEXISTENTE
curl -s -X POST http://localhost:8080/treinos/999/execucoes -H "Content-Type: application/json" -d "{\"dataHora\":\"2026-06-29T14:30:00\",\"exercicios\":[{\"exercicioId\":1,\"series\":1,\"repeticoes\":\"1\",\"carga\":10}]}"
echo.
echo.

echo [12] BUSCAR EXECUCAO ID=2
curl -s http://localhost:8080/execucoes/2
echo.
echo.

echo [13] BUSCAR EXECUCAO INEXISTENTE
curl -s http://localhost:8080/execucoes/999
echo.
echo.

echo [14] CRIAR EXECUCAO COM EXERCICIO DE OUTRO TREINO (validacao)
curl -s -X POST http://localhost:8080/treinos/1/execucoes -H "Content-Type: application/json" -d "{\"dataHora\":\"2026-06-29T14:30:00\",\"exercicios\":[{\"exercicioId\":4,\"series\":1,\"repeticoes\":\"1\",\"carga\":10}]}"
echo.
echo.

echo [15] DELETAR EXECUCAO ID=1
curl -s -X DELETE http://localhost:8080/execucoes/1
echo.
echo.

echo [16] DELETAR TREINO PERNAS (ID=2)
curl -s -X DELETE http://localhost:8080/treinos/2
echo.
echo.

echo [17] DELETAR TREINO COSTAS (ID=3) COM CASCADE
curl -s -X DELETE http://localhost:8080/treinos/3
echo.
echo.

echo [18] TREINOS RESTANTES
curl -s http://localhost:8080/treinos
echo.
echo.

echo ========================================
echo   FIM
echo ========================================
