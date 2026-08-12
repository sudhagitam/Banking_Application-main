import React, { useEffect, useState } from 'react';
import { getAccountDetails, makeTransfer } from '../services/api';

export default function Dashboard({ onLogout }) {
  const [account, setAccount] = useState(null);
  const [transferData, setTransferData] = useState({ receiverAccount: '', amount: '' });
  const [msg, setMsg] = useState('');

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
    try {
      await makeTransfer(transferData);
      setMsg('Transfer successful!');
      fetchAccount(); // Refresh balance
    } catch (err) {
      setMsg('Transfer failed.');
    }
  };

  return (
    <div style={{ padding: '20px', maxWidth: '600px', margin: 'auto' }}>
      <button onClick={onLogout} style={{ float: 'right' }}>Logout</button>
      <h2>Account Dashboard</h2>
      {account ? (
        <div>
          <p><strong>Account Number:</strong> {account.accountNumber}</p>
          <p><strong>Type:</strong> {account.accountType}</p>
          <p><strong>Balance:</strong> ${account.balance}</p>
        </div>
      ) : (
        <p>Loading account details...</p>
      )}

      <hr />
      <h3>Transfer Funds</h3>
      {msg && <p>{msg}</p>}
      <form onSubmit={handleTransfer}>
        <input
          type="number"
          placeholder="Recipient Account Number"
          style={{ width: '100%', marginBottom: '10px' }}
          value={transferData.receiverAccount}
          onChange={(e) => setTransferData({ ...transferData, receiverAccount: parseInt(e.target.value || '0', 10) })}
          required
        />
        <input
          type="number"
          placeholder="Amount"
          style={{ width: '100%', marginBottom: '10px' }}
          value={transferData.amount}
          onChange={(e) => setTransferData({ ...transferData, amount: parseFloat(e.target.value || '0') })}
          required
        />
        <button type="submit" style={{ width: '100%', padding: '10px' }}>Send Money</button>
      </form>
    </div>
  );
}