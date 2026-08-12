import axios from 'axios';

const API = axios.create({
  baseURL: 'http://localhost:8080/api',
});

// Automatically attach JWT Token if logged in
API.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const loginUser = (credentials) => API.post('/auth/login', credentials);
export const registerUser = (userData) => API.post('/auth/register', userData);
export const getAccountDetails = () => API.get('/account/details');
export const makeTransfer = (data) => API.post('/transaction/transfer', data);

// Password Recovery Endpoints
export const forgotPassword = (email) => API.post('/auth/forgot-password', { email });
export const verifyOtp = (email, otp) => API.post('/auth/verify-otp', { email, otp });
export const resetPassword = (email, password) => API.post('/auth/reset-password', { email, password });

// Transaction Endpoints
export const getTransactionHistory = () => API.get('/account/transactions');
export const depositMoney = (amount) => API.post('/transaction/CREDIT', {}, { params: { amount } });
export const withdrawMoney = (amount) => API.post('/transaction/DEBIT', {}, { params: { amount } });

export default API;
