import axios from 'axios';
import { createApi } from '../integration/httpClient';

const authApi = createApi(
  `${process.env.EXPO_PUBLIC_LOCAL_API_URL}/fatec/login/v1`
);

const api = createApi(
  `${process.env.EXPO_PUBLIC_LOCAL_API_URL}/api-pokemon/auth/v1`
);

export type RegistroRequest = {
  username: string;
  password: string;
  email: string;
  cep: string;
  roles: string[];
}

export type AuthRequest = {
  username: string;
  password: string;
};

// Bate com o novo AuthResponse do Spring: sem token, ele vive no cookie
export type AuthResponse = {
    userId: string;
    username: string;
};

export type StatsResponse = {
  userId: string;
  username: string;
  level: number;
  vitorias: number;
  derrotas: number;
};

export type UpdateStatsRequest = {
  level: string;
  vitorias: string;
  derrotas: string;
};

export type TeamUpdateRequest = {
  removedPokemon: number;
  newPokemon: number;
};

// ===== AUTH =====

export const register = async (data: RegistroRequest): Promise<void> => {
    await authApi.post('/user/save', data);
};

export const login = async (data: AuthRequest): Promise<AuthResponse> => {
    const response = await authApi.post('/auth', data);
    return response.data;
};

// Confirma se o cookie ainda é válido e quem está logado.
// Usado no boot do app, em vez de decodificar JWT local.
export const getMe = async (): Promise<AuthResponse> => {
    const response = await authApi.get('/me');
    return response.data;
};

export const logout = async (): Promise<void> => {
    await authApi.post('/logout');
};

export const getProfile = async (userId: string): Promise<StatsResponse> => {
  const response = await authApi.get(`/stats/${userId}`);
  return response.data;
};

export const updateProfile = async (
  userId: string,
  data: UpdateStatsRequest
): Promise<StatsResponse> => {
  const response = await authApi.put(`/stats/${userId}`, data);
  return response.data;
};

// ===== POKÉMON =====
// ATENÇÃO: essas rotas apontam pra "/api-pokemon/auth/v1", que NÃO existe
// nesse projeto Spring (ele só tem AuthController + StatsController).
// Deixe assim por enquanto - fica pra quando esse serviço existir.

export const getTeam = async (userId: string) => {
  const response = await api.get('/pokemon/v1/team', {
    params: { 'user-id': userId },
  });
  return response.data;
};

export const updateTeam = async (userId: string, data: TeamUpdateRequest) => {
  const response = await api.put('/pokemon/v1/team', data, {
    params: { 'user-id': userId },
  });
  return response.data;
};

export const addCaptured = async (userId: string, pokemonId: number) => {
  const response = await api.put('/pokemon/v1/captured', null, {
    params: { 'user-id': userId, 'pokemon-id': pokemonId },
  });
  return response.data;
};

export const deleteCaptured = async (userId: string, pokemonId: number) => {
  const response = await api.delete('/pokemon/v1/captured', {
    params: { 'user-id': userId, 'pokemon-id': pokemonId },
  });
  return response.data;
};