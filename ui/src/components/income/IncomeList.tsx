import React from 'react';
import { formatCurrency } from '../../utils/currency';
import type { Income } from '../../types';
import { Edit3, X, TrendingUp } from 'lucide-react';
import Card from '../common/Card';

interface IncomeListProps {
  income: Income[];
  currency: 'USD' | 'EUR';
  onEdit: (income: Income) => void;
  onDelete: (id: number) => void;
  deletingId?: number | null;
}

const IncomeList: React.FC<IncomeListProps> = ({ income, currency, onEdit, onDelete, deletingId }) => {
  const formatNextPayment = (nextPaymentDate: string) => {
    const paymentDate = new Date(nextPaymentDate);
    const now = new Date();
    const diffInDays = Math.ceil((paymentDate.getTime() - now.getTime()) / (1000 * 60 * 60 * 24));
    
    const formattedDate = paymentDate.toLocaleDateString();
    
    if (diffInDays === 0) return `Next payment: ${formattedDate} (today)`;
    if (diffInDays === 1) return `Next payment: ${formattedDate} (tomorrow)`;
    if (diffInDays > 1) return `Next payment: ${formattedDate} (${diffInDays} days)`;
    return `Next payment: ${formattedDate} (${Math.abs(diffInDays)} days ago)`;
  };

  return (
    <div className="grid-gap">
      {income.map((entry) => {
        const statusText = entry.nextPaymentDate 
          ? formatNextPayment(entry.nextPaymentDate)
          : `Received ${new Date(entry.incomeDate).toLocaleDateString()}`;
        const statusColor = '#10b981'; // Green color for income

        const actions = [
          <button
            key="edit"
            onClick={() => onEdit(entry)}
            className="p-3 rounded-md text-slate-400 hover:text-blue-400 hover:bg-blue-400/10 transition-colors"
            title="Edit income"
          >
            <Edit3 size={18} />
          </button>,
          <button
            key="delete"
            onClick={() => onDelete(entry.id)}
            disabled={deletingId === entry.id}
            className="p-3 rounded-md text-slate-400 hover:text-red-400 hover:bg-red-400/10 transition-colors disabled:opacity-50"
            title="Delete income"
          >
            <X size={18} />
          </button>
        ];

        // Build subtitle with category and description
        let subtitle = entry.categoryName || 'Uncategorized';
        if (entry.description) {
          subtitle += ` • ${entry.description}`;
        }

        return (
          <Card
            key={entry.id}
            title={entry.name}
            subtitle={subtitle}
            status={{
              text: statusText,
              color: statusColor
            }}
            icon={<TrendingUp size={36} />}
            amount={formatCurrency(entry.amount, currency)}
            amountLabel="income"
            actions={actions}
            theme="income"
          />
        );
      })}
    </div>
  );
};

export default IncomeList;