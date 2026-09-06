import React, { createContext, useState, useContext, useEffect } from "react";
import { router } from "expo-router";
import {
    login as loginApi,
    register,
    logout as logoutApi,
    getMe,
    RegistroRequest
} from "@/services/pokemonApi";
import { setUnauthorizeHandler } from "@/integration/httpClient";

type AuthContextData = {
    isAuthenticated: boolean;
    user: string | null;
    userId: string | null;
    isLoading: boolean;

    signIn: (
        username: string,
        password: string
    ) => Promise<{ ok: boolean; userId?: string }>;

    signUp: (
        data: RegistroRequest
    ) => Promise<{
        ok: boolean;
        userId?: string;
        error?: string;
    }>;

    signOut: () => void;
};

const AuthContext = createContext({} as AuthContextData);

export const AuthProvider = ({
    children
}: {
    children: React.ReactNode;
}) => {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [user, setUser] = useState<string | null>(null);
    const [userId, setUserId] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState(true);

    function applySession(data: { userId: string; username: string }) {
        setUser(data.username);
        setUserId(data.userId);
        setIsAuthenticated(true);
    }

    function clearSession() {
        setUser(null);
        setUserId(null);
        setIsAuthenticated(false);
    }

    useEffect(() => {
        // Não tem mais JWT local pra decodificar/checar expiração.
        // A única forma confiável de saber se o cookie ainda é válido
        // é perguntar pro backend.
        async function loadSession() {
            try {
                const me = await getMe();
                applySession(me);
            } catch {
                clearSession();
            } finally {
                setIsLoading(false);
            }
        }

        loadSession();
    }, []);

    useEffect(() => {
        setUnauthorizeHandler(() => {
            clearSession();
            router.replace("/");
        });
    }, []);

    async function signIn(
        username: string,
        password: string
    ): Promise<{ ok: boolean; userId?: string }> {
        try {
            const response = await loginApi({ username, password });
            applySession(response);

            return {
                ok: true,
                userId: response.userId
            };
        } catch (error) {
            return { ok: false };
        }
    }

    async function signUp(
        data: RegistroRequest
    ): Promise<{
        ok: boolean;
        userId?: string;
        error?: string;
    }> {
        try {
            await register(data);
            return { ok: true };
        } catch (err: any) {
            const message =
                err.response?.data?.message ||
                "Erro desconhecido";

            return { ok: false, error: message };
        }
    }

    async function signOut() {
        try {
            await logoutApi();
        } finally {
            clearSession();
        }
    }

    return (
        <AuthContext.Provider
            value={{
                isAuthenticated,
                user,
                userId,
                signIn,
                signUp,
                signOut,
                isLoading
            }}
        >
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);