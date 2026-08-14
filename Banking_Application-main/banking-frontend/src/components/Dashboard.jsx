import React, { useEffect, useState } from 'react';
import { getAccountDetails, makeTransfer } from '../services/api';
import TransactionHistory from './TransactionHistory';

export default function Dashboard({ onLogout }) {
  const [account, setAccount] = useState(null);
  const [transferData, setTransferData] = useState({ receiverAccount: '', amount: '' });
  const [msg, setMsg] = useState('');
  const [refreshTxKey, setRefreshTxKey] = useState(0);

  useEffect(() => {
    fetchAccount();
  }, []);

  const fetchAccount = async () => {
    try {
      const res = await getAccountDetails();
      setAccount(res.data);
    } catch (err) {
      console.error('Failed to fetch account', err);
    }
  };

  const handleTransfer = async (e) => {
    e.preventDefault();
    setMsg('');

    const payload = {
      receiverAccount: Number(transferData.receiverAccount),
      amount: Number(transferData.amount)
    };

    if (!payload.receiverAccount || payload.amount <= 0) {
      setMsg('Please enter a valid account number and amount greater than 0.');
      return;
    }

    try {
      await makeTransfer(payload);
      setMsg('Transfer successful!');
      setTransferData({ receiverAccount: '', amount: '' });

      // Refresh account balance & force transaction history table update
      fetchAccount();
      setRefreshTxKey((prevKey) => prevKey + 1);
    } catch (err) {
      const errorMsg = err.response?.data?.message || err.response?.data?.error || 'Transfer failed.';
      setMsg(errorMsg);
    }
  };

  return (
    <div style={{ padding: '20px', maxWidth: '650px', margin: 'auto', fontFamily: 'sans-serif' }}>
      <button
        onClick={onLogout}
        style={{ float: 'right', padding: '6px 12px', cursor: 'pointer', backgroundColor: '#dc3545', color: '#fff', border: 'none', borderRadius: '4px' }}
      >
        Logout
      </button>
      <h2>Account Dashboard</h2>

      {account ? (
        <div style={{ backgroundColor: '#f9f9f9', padding: '15px', borderRadius: '6px', marginBottom: '20px', boxShadow: '0 1px 3px rgba(0,0,0,0.1)' }}>
          <p style={{ margin: '5px 0' }}><strong>Account Number:</strong> {account.accountNumber}</p>
          <p style={{ margin: '5px 0' }}><strong>Type:</strong> {account.accountType}</p>
          <p style={{ margin: '5px 0', fontSize: '18px' }}><strong>Balance:</strong> <span style={{ color: '#2e7d32', fontWeight: 'bold' }}>${account.balance?.toFixed(2) || '0.00'}</span></p>
        </div>
      ) : (
        <p>Loading account details...</p>
      )}

      <hr style={{ margin: '20px 0', border: 'none', borderTop: '1px solid #ddd' }} />

      <h3>Transfer Funds</h3>
      {msg && (
        <p style={{ color: msg.includes('successful') ? 'green' : 'red', fontWeight: 'bold' }}>
          {msg}
        </p>
      )}

      <form onSubmit={handleTransfer}>
        <div style={{ marginBottom: '10px' }}>
          <input
            type="number"
            placeholder="Recipient Account Number"
            style={{ width: '100%', padding: '8px', boxSizing: 'border-box', borderRadius: '4px', border: '1px solid #ccc' }}
            value={transferData.receiverAccount}
            onChange={(e) => setTransferData({ ...transferData, receiverAccount: e.target.value })}
            required
          />
        </div>

        <div style={{ marginBottom: '10px' }}>
          <input
            type="number"
            step="0.01"
            placeholder="Amount"
            style={{ width: '100%', padding: '8px', boxSizing: 'border-box', borderRadius: '4px', border: '1px solid #ccc' }}
            value={transferData.amount}
            onChange={(e) => setTransferData({ ...transferData, amount: e.target.value })}
            required
          />
        </div>

        <button
          type="submit"
          style={{ width: '100%', padding: '10px', backgroundColor: '#007bff', color: '#fff', border: 'none', borderRadius: '4px', cursor: 'pointer', fontWeight: 'bold' }}
        >
          Send Money
        </button>
      </form>

      <hr style={{ margin: '30px 0', border: 'none', borderTop: '1px solid #ddd' }} />

      {/* Passing key forces TransactionHistory to reload upon new transfers */}
      <TransactionHistory key={refreshTxKey} />
    </div>
  );
}