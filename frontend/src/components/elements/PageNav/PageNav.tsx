import "./PageNav.css";

interface PageNavProps {
  page: number;
  size: number;
  onPageChange: (page: number) => void;
  entitiesAmount: number
}

export const PageNav = ({
  page,
  size,
  onPageChange,
  entitiesAmount
}: PageNavProps) => {
  return (
    <div className="page-nav-container">
        <button className="page-nav-button" onClick={() => onPageChange(page - 1)}>Back</button>
      
      <div className="page-nav-numbers">Page {page + 1} ({Math.min(size * page + 1, entitiesAmount)} - {Math.min(size * (page + 1), entitiesAmount)} of {entitiesAmount})</div>
      <button className="page-nav-button" onClick={() => onPageChange(page + 1)}>Next</button>
      
    </div>
  );
};
